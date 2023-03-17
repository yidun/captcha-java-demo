/* eslint-disable no-undef */
(function (global, factory) {
  'use strict'
  if (typeof module === 'object' && typeof module.exports === 'object') {
    module.exports = global.document ? factory(global) : function (w) {
      if (!w.document) {
        throw new Error('initNECaptchaWithFallback requires a window with a document')
      }
      return factory(w)
    }
  } else if (typeof define === 'function' && define.amd) {
    define('initNECaptchaWithFallback', [], function () {
      return factory(global)
    })
  } else {
    global.initNECaptchaWithFallback = factory(global)
  }
}(typeof window !== 'undefined' ? window : this, function (window) {
  'use strict'
  var errorCallbackCount = 0

  // 常量
  var DEFAULT_VALIDATE = 'QjGAuvoHrcpuxlbw7cp4WnIbbjzG4rtSlpc7EDovNHQS._ujzPZpeCInSxIT4WunuDDh8dRZYF2GbBGWyHlC6q5uEi9x-TXT9j7J705vSsBXyTar7aqFYyUltKYJ7f4Y2TXm_1Mn6HFkb4M7URQ_rWtpxQ5D6hCgNJYC0HpRE7.2sttqYKLoi7yP1KHzK-PptdHHkVwb77cwS2EJW7Mj_PsOtnPBubTmTZLpnRECJR99dWTVC11xYG0sx8dJNLUxUFxEyzTfX4nSmQz_T5sXATRKHtVAz7nmV0De5unmflfAlUwMGKlCT1khBtewlgN5nHvyxeD8Z1_fPVzi9oznl-sbegj6lKfCWezmLcwft8.4yaVh6SlzXJq-FnSK.euq9OBd5jYc82ge2_hEca1fGU--SkPRzgwkzew4O4qjdS2utdPwFONnhKAIMJRPUmCV4lPHG1OeRDvyNV8sCnuFMw7leasxIhPoycl4pm5bNy70Z1laozEGJgItVNr3' // 默认validate
  var FALLBACK_LANG = {
    'zh-CN': '前方拥堵，已自动跳过验证',
    'en': 'captcha error，Verified automatically'
  }
  var CACHE_MIN = 1000 * 60 // 缓存时长单位，1分钟
  var REQUEST_SCRIPT_ERROR = 502

  var RESOURCE_CACHE = {}

  // 工具函数
  function loadScript (src, cb) {
    var head = document.head || document.getElementsByTagName('head')[0]
    var script = document.createElement('script')

    cb = cb || function () {}

    script.type = 'text/javascript'
    script.charset = 'utf8'
    script.async = true
    script.src = src

    if (!('onload' in script)) {
      script.onreadystatechange = function () {
        if (this.readyState !== 'complete' && this.readyState !== 'loaded') return
        this.onreadystatechange = null
        cb(null, script) // there is no way to catch loading errors in IE8
      }
    }

    script.onload = function () {
      this.onerror = this.onload = null
      cb(null, script)
    }
    script.onerror = function () {
      // because even IE9 works not like others
      this.onerror = this.onload = null
      cb(new Error('Failed to load ' + this.src), script)
    }

    head.appendChild(script)
  }

  function joinUrl (protocol, host, path) {
    protocol = protocol || ''
    host = host || ''
    path = path || ''
    if (protocol) {
      protocol = protocol.replace(/:?\/{0,2}$/, '://')
    }
    if (host) {
      var matched = host.match(/^([-0-9a-zA-Z.:]*)(\/.*)?/)
      host = matched[1]
      path = (matched[2] || '') + '/' + path
    }
    !host && (protocol = '')

    return protocol + host + path
  }

  function setDomText (el, value) {
    if (value === undefined) return
    var nodeType = el.nodeType
    if (nodeType === 1 || nodeType === 11 || nodeType === 9) {
      if (typeof el.textContent === 'string') {
        el.textContent = value
      } else {
        el.innerText = value
      }
    }
  }

  function queryAllByClassName (selector, node) {
    node = node || document
    if (node.querySelectorAll) {
      return node.querySelectorAll(selector)
    }
    if (!/^\.[^.]+$/.test(selector)) return []
    if (node.getElementsByClassName) {
      return node.getElementsByClassName(selector)
    }

    var children = node.getElementsByTagName('*')
    var current
    var result = []
    var className = selector.slice(1)
    for (var i = 0, l = children.length; i < l; i++) {
      current = children[i]
      if (~(' ' + current.className + ' ').indexOf(' ' + className + ' ')) {
        result.push(current)
      }
    }
    return result
  }

  function assert (condition, msg) {
    if (!condition) throw new Error('[NECaptcha] ' + msg)
  }

  function isInteger (val) {
    if (Number.isInteger) {
      return Number.isInteger(val)
    }
    return typeof val === 'number' && isFinite(val) && Math.floor(val) === val
  }

  function isArray (val) {
    if (Array.isArray) return Array.isArray(val)
    return Object.prototype.toString.call(val) === '[object Array]'
  }

  function ObjectAssign () {
    if (Object.assign) {
      return Object.assign.apply(null, arguments)
    }

    var target = {}
    for (var index = 1; index < arguments.length; index++) {
      var source = arguments[index]
      if (source != null) {
        for (var key in source) {
          if (Object.prototype.hasOwnProperty.call(source, key)) {
            target[key] = source[key]
          }
        }
      }
    }
    return target
  }

  function getTimestamp (msec) {
    msec = !msec && msec !== 0 ? msec : 1
    return parseInt((new Date()).valueOf() / msec, 10)
  }

  // 降级方案
  function normalizeFallbackConfig (customConfig) {
    var siteProtocol = window.location.protocol.replace(':', '')
    var defaultConf = {
      protocol: siteProtocol === 'http' ? 'http' : 'https',
      lang: 'zh-CN',
      errorFallbackCount: 3
    }
    var config = ObjectAssign({}, defaultConf, customConfig)

    var errorFallbackCount = config.errorFallbackCount
    assert(
      errorFallbackCount === undefined || (isInteger(errorFallbackCount) && errorFallbackCount >= 1),
      'errorFallbackCount must be an integer, and it\'s value greater than or equal one')

    return config
  }

  function loadResource (config, cb) {
    if (window.initNECaptcha) {
      setTimeout(function () {
        cb(null)
      }, 0)
      return
    }
    function genUrl (server) {
      var path = 'load.min.js'
      var urls = []
      if (isArray(server)) {
        for (var i = 0, len = server.length; i < len; i++) {
          urls.push(joinUrl(config.protocol, server[i], path))
        }
      } else {
        var url = joinUrl(config.protocol, server, path)
        urls = [url, url]
      }

      return urls
    }
    
    const defaultStaticServer = config.ipv6 ? [
      'cstaticdun-v6.126.net',
      'cstaticdun.126.net'
    ] : [
      'cstaticdun.126.net',
      'cstaticdun1.126.net'
    ]
    var urls = genUrl(config.staticServer || defaultStaticServer)

    function step (i) {
      var url = urls[i] + '?v=' + getTimestamp(CACHE_MIN)
      loadScript(url, function (err) {
        if (err || !window.initNECaptcha) { // loadjs的全局变量
          i = i + 1
          if (i === urls.length) {
            return cb(new Error('Failed to load script(' + url + ').' + (err ? err.message : 'unreliable script')))
          }
          return step(i)
        }
        return cb(null)
      })
    }
    step(0)
  }

  /*
   * entry: initNECaptchaWithFallback
   * options: 
   *  errorFallbackCount: 触发降级的错误次数，默认第三次错误降级
   *  defaultFallback: 是否开启默认降级
   *  onFallback: 自定义降级方案，参数为默认validate
  */
  function initNECaptchaWithFallback (options, onload, onerror) {
    var captchaIns = null

    var config = normalizeFallbackConfig(options)
    var defaultFallback = config.defaultFallback !== false
    var langPkg = FALLBACK_LANG[config.lang === 'zh-CN' ? config.lang : 'en']
    var storeKey = window.location.pathname + '_' + config.captchaId + '_NECAPTCHA_ERROR_COUNTS'
    try {
      errorCallbackCount = parseInt(localStorage.getItem(storeKey) || 0, 10)
    } catch (error) {}

    var fallbackFn = !defaultFallback ? config.onFallback || function () {} : function (validate) {
      function setFallbackTip (instance) {
        if (!instance) return
        setFallbackTip(instance._captchaIns)
        if (!instance.$el) return
        var tipEles = queryAllByClassName('.yidun-fallback__tip', instance.$el)
        if (!tipEles.length) return

        // 确保在队列的最后
        setTimeout(function () {
          for (var i = 0, l = tipEles.length; i < l; i++) {
            setDomText(tipEles[i], langPkg)
          }
        }, 0)
      }
      setFallbackTip(captchaIns)

      config.onVerify && config.onVerify(null, { validate: validate })
    }
    var noFallback = !defaultFallback && !config.onFallback

    var proxyOnError = function (error) {
      errorCallbackCount++
      if (errorCallbackCount < config.errorFallbackCount) {
        try {
          localStorage.setItem(storeKey, errorCallbackCount)
        } catch (err) {}

        onerror(error)
      } else {
        fallbackFn(DEFAULT_VALIDATE)
        proxyRefresh()
        noFallback && onerror(error)
      }
    }

    var proxyRefresh = function () {
      errorCallbackCount = 0
      try {
        localStorage.setItem(storeKey, 0)
      } catch (err) {}
    }

    var triggerInitError = function (error) {
      if (initialTimer && initialTimer.isError()) {
        initialTimer.resetError()
        return
      }
      initialTimer && initialTimer.resetTimer()
      noFallback ? onerror(error) : proxyOnError(error)
    }

    config.onError = function (error) {
      if (initialTimer && initialTimer.isError()) {
        initialTimer.resetError()
      }
      proxyOnError(error)
    }
    config.onDidRefresh = function () {
      if (initialTimer && initialTimer.isError()) {
        initialTimer.resetError()
      }
      proxyRefresh()
    }

    var initialTimer = options.initTimeoutError ? options.initTimeoutError(proxyOnError) : null // initialTimer is only for mobile.html

    var loadResolve = function () {
      window.initNECaptcha(config, function (instance) {
        if (initialTimer && initialTimer.isError()) return
        initialTimer && initialTimer.resetTimer()
        captchaIns = instance
        onload && onload(instance)
      }, triggerInitError)
    }
    var cacheId = 'load-queue'
    if (!RESOURCE_CACHE[cacheId]) {
      RESOURCE_CACHE[cacheId] = {
        rejects: [],
        resolves: [],
        status: 'error'
      }
    }
    if (RESOURCE_CACHE[cacheId].status === 'error') {
      RESOURCE_CACHE[cacheId].status = 'pending'
      loadResource(config, function (error) {
        if (error) {
          var err = new Error()
          err.code = REQUEST_SCRIPT_ERROR
          err.message = config.staticServer + '/load.min.js error'

          var rejects = RESOURCE_CACHE[cacheId].rejects
          for (var i = 0, iLen = rejects.length; i < iLen; i++) {
            rejects.pop()(err)
          }
          RESOURCE_CACHE[cacheId].status = 'error'
        } else {
          RESOURCE_CACHE[cacheId].status = 'done'
          var resolves = RESOURCE_CACHE[cacheId].resolves
          for (var j = 0, jLen = resolves.length; j < jLen; j++) {
            resolves.pop()()
          }
        }
      })
    } else if (RESOURCE_CACHE[cacheId].status === 'done') {
      loadResolve()
    }
    if (RESOURCE_CACHE[cacheId].status === 'pending') {
      RESOURCE_CACHE[cacheId].rejects.push(function loadReject (err) {
        triggerInitError(err)
      })
      RESOURCE_CACHE[cacheId].resolves.push(loadResolve)
    }
  }

  return initNECaptchaWithFallback
}))
