"use strict";

function _inherits(e, t) {
	if ("function" != typeof t && null !== t) throw new TypeError("Super expression must either be null or a function");
	e.prototype = Object.create(t && t.prototype, {
		constructor: {
			value: e,
			writable: !0,
			configurable: !0
		}
	}), Object.defineProperty(e, "prototype", {
		writable: !1
	}), t && _setPrototypeOf(e, t)
}

function _setPrototypeOf(e, t) {
	return (_setPrototypeOf = Object.setPrototypeOf ? Object.setPrototypeOf.bind() : function (e, t) {
		return e.__proto__ = t, e
	})(e, t)
}

function _createSuper(a) {
	var i = _isNativeReflectConstruct();
	return function () {
		var e, t = _getPrototypeOf(a);
		return _possibleConstructorReturn(this, i ? (e = _getPrototypeOf(this).constructor, Reflect.construct(t, arguments, e)) : t.apply(this, arguments))
	}
}

function _possibleConstructorReturn(e, t) {
	if (t && ("object" === _typeof(t) || "function" == typeof t)) return t;
	if (void 0 !== t) throw new TypeError("Derived constructors may only return object or undefined");
	return _assertThisInitialized(e)
}

function _assertThisInitialized(e) {
	if (void 0 === e) throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
	return e
}

function _isNativeReflectConstruct() {
	if ("undefined" == typeof Reflect || !Reflect.construct) return !1;
	if (Reflect.construct.sham) return !1;
	if ("function" == typeof Proxy) return !0;
	try {
		return Boolean.prototype.valueOf.call(Reflect.construct(Boolean, [], function () {})), !0
	} catch (e) {
		return !1
	}
}

function _getPrototypeOf(e) {
	return (_getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf.bind() : function (e) {
		return e.__proto__ || Object.getPrototypeOf(e)
	})(e)
}

function _defineProperty(e, t, a) {
	return t in e ? Object.defineProperty(e, t, {
		value: a,
		enumerable: !0,
		configurable: !0,
		writable: !0
	}) : e[t] = a, e
}

function _classCallCheck(e, t) {
	if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
}

function _defineProperties(e, t) {
	for (var a = 0; a < t.length; a++) {
		var i = t[a];
		i.enumerable = i.enumerable || !1, i.configurable = !0, "value" in i && (i.writable = !0), Object.defineProperty(e, i.key, i)
	}
}

function _createClass(e, t, a) {
	return t && _defineProperties(e.prototype, t), a && _defineProperties(e, a), Object.defineProperty(e, "prototype", {
		writable: !1
	}), e
}

function _typeof(e) {
	return (_typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
		return typeof e
	} : function (e) {
		return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
	})(e)
}! function () {
	var a = [, function (A, e, t) {
			t.r(e), t.d(e, {
				default: function () {
					return ie
				}
			});
			var E = t(2),
				U = t(4),
				D = {
					addClass: E.addClass,
					removeClass: E.removeClass,
					hasClass: E.hasClass,
					toggleClass: E.toggleClass,
					attr: E.attr,
					removeAttr: E.removeAttr,
					data: E.data,
					transform: E.transform,
					transition: E.transition,
					on: E.on,
					off: E.off,
					trigger: E.trigger,
					transitionEnd: E.transitionEnd,
					outerWidth: E.outerWidth,
					outerHeight: E.outerHeight,
					offset: E.offset,
					css: E.css,
					each: E.each,
					html: E.html,
					text: E.text,
					is: E.is,
					index: E.index,
					eq: E.eq,
					append: E.append,
					prepend: E.prepend,
					next: E.next,
					nextAll: E.nextAll,
					prev: E.prev,
					prevAll: E.prevAll,
					parent: E.parent,
					parents: E.parents,
					closest: E.closest,
					find: E.find,
					children: E.children,
					filter: E.filter,
					remove: E.remove,
					add: E.add,
					styles: E.styles
				},
				K = (Object.keys(D).forEach(function (e) {
					E.$.fn[e] = E.$.fn[e] || D[e]
				}), {
					deleteProps: function (e) {
						var t = e;
						Object.keys(t).forEach(function (e) {
							try {
								t[e] = null
							} catch (e) {}
							try {
								delete t[e]
							} catch (e) {}
						})
					},
					nextTick: function (e) {
						return setTimeout(e, 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : 0)
					},
					now: function () {
						return Date.now()
					},
					getTranslate: function (e) {
						var t, a, i, n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : "x",
							e = U.window.getComputedStyle(e, null);
						return U.window.WebKitCSSMatrix ? (6 < (a = e.transform || e.webkitTransform).split(",").length && (a = a.split(", ").map(function (e) {
							return e.replace(",", ".")
						}).join(", ")), i = new U.window.WebKitCSSMatrix("none" === a ? "" : a)) : t = (i = e.MozTransform || e.OTransform || e.MsTransform || e.msTransform || e.transform || e.getPropertyValue("transform").replace("translate(", "matrix(1, 0, 0, 1,")).toString().split(","), "x" === n && (a = U.window.WebKitCSSMatrix ? i.m41 : 16 === t.length ? parseFloat(t[12]) : parseFloat(t[4])), (a = "y" === n ? U.window.WebKitCSSMatrix ? i.m42 : 16 === t.length ? parseFloat(t[13]) : parseFloat(t[5]) : a) || 0
					},
					parseUrlQuery: function (e) {
						var t, a, i, n, s = {},
							e = e || U.window.location.href;
						if ("string" == typeof e && e.length)
							for (n = (a = (e = -1 < e.indexOf("?") ? e.replace(/\S*\?/, "") : "").split("&").filter(function (e) {
									return "" !== e
								})).length, t = 0; t < n; t += 1) i = a[t].replace(/#\S+/g, "").split("="), s[decodeURIComponent(i[0])] = void 0 === i[1] ? void 0 : decodeURIComponent(i[1]) || "";
						return s
					},
					isObject: function (e) {
						return "object" === _typeof(e) && null !== e && e.constructor && e.constructor === Object
					},
					extend: function () {
						for (var e = Object(arguments.length <= 0 ? void 0 : arguments[0]), t = 1; t < arguments.length; t += 1) {
							var a = t < 0 || arguments.length <= t ? void 0 : arguments[t];
							if (null != a)
								for (var i = Object.keys(Object(a)), n = 0, s = i.length; n < s; n += 1) {
									var r = i[n],
										o = Object.getOwnPropertyDescriptor(a, r);
									void 0 !== o && o.enumerable && (K.isObject(e[r]) && K.isObject(a[r]) ? K.extend(e[r], a[r]) : !K.isObject(e[r]) && K.isObject(a[r]) ? (e[r] = {}, K.extend(e[r], a[r])) : e[r] = a[r])
								}
						}
						return e
					}
				}),
				b = {
					touch: U.window.Modernizr && !0 === U.window.Modernizr.touch || !!(0 < U.window.navigator.maxTouchPoints || "ontouchstart" in U.window || U.window.DocumentTouch && U.document instanceof U.window.DocumentTouch),
					pointerEvents: !!U.window.PointerEvent && "maxTouchPoints" in U.window.navigator && 0 < U.window.navigator.maxTouchPoints,
					observer: "MutationObserver" in U.window || "WebkitMutationObserver" in U.window,
					passiveListener: function () {
						var e = !1;
						try {
							var t = Object.defineProperty({}, "passive", {
								get: function () {
									e = !0
								}
							});
							U.window.addEventListener("testPassiveListener", null, t)
						} catch (e) {}
						return e
					}(),
					gestures: "ongesturestart" in U.window
				},
				N = function () {
					function a() {
						var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {},
							t = (_classCallCheck(this, a), this);
						t.params = e, t.eventsListeners = {}, t.params && t.params.on && Object.keys(t.params.on).forEach(function (e) {
							t.on(e, t.params.on[e])
						})
					}
					return _createClass(a, [{
						key: "on",
						value: function (e, t, a) {
							var i, n = this;
							return "function" == typeof t && (i = a ? "unshift" : "push", e.split(" ").forEach(function (e) {
								n.eventsListeners[e] || (n.eventsListeners[e] = []), n.eventsListeners[e][i](t)
							})), n
						}
					}, {
						key: "once",
						value: function (i, n, e) {
							var s = this;
							return "function" != typeof n ? s : (r.f7proxy = n, s.on(i, r, e));

							function r() {
								s.off(i, r), r.f7proxy && delete r.f7proxy;
								for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
								n.apply(s, t)
							}
						}
					}, {
						key: "off",
						value: function (e, i) {
							var n = this;
							return n.eventsListeners && e.split(" ").forEach(function (a) {
								void 0 === i ? n.eventsListeners[a] = [] : n.eventsListeners[a] && n.eventsListeners[a].length && n.eventsListeners[a].forEach(function (e, t) {
									(e === i || e.f7proxy && e.f7proxy === i) && n.eventsListeners[a].splice(t, 1)
								})
							}), n
						}
					}, {
						key: "emit",
						value: function () {
							var a = this;
							if (a.eventsListeners) {
								for (var e, i, t = arguments.length, n = new Array(t), s = 0; s < t; s++) n[s] = arguments[s];
								var r = "string" == typeof n[0] || Array.isArray(n[0]) ? (e = n[0], i = n.slice(1, n.length), a) : (e = n[0].events, i = n[0].data, n[0].context || a);
								(Array.isArray(e) ? e : e.split(" ")).forEach(function (e) {
									var t;
									a.eventsListeners && a.eventsListeners[e] && (t = [], a.eventsListeners[e].forEach(function (e) {
										t.push(e)
									}), t.forEach(function (e) {
										e.apply(r, i)
									}))
								})
							}
							return a
						}
					}, {
						key: "useModulesParams",
						value: function (t) {
							var a = this;
							a.modules && Object.keys(a.modules).forEach(function (e) {
								e = a.modules[e];
								e.params && K.extend(t, e.params)
							})
						}
					}, {
						key: "useModules",
						value: function () {
							var t = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {},
								i = this;
							i.modules && Object.keys(i.modules).forEach(function (e) {
								var a = i.modules[e],
									e = t[e] || {};
								a.instance && Object.keys(a.instance).forEach(function (e) {
									var t = a.instance[e];
									i[e] = "function" == typeof t ? t.bind(i) : t
								}), a.on && i.on && Object.keys(a.on).forEach(function (e) {
									i.on(e, a.on[e])
								}), a.create && a.create.bind(i)(e)
							})
						}
					}], [{
						key: "components",
						set: function (e) {
							this.use && this.use(e)
						}
					}, {
						key: "installModule",
						value: function (t) {
							var a = this,
								e = (a.prototype.modules || (a.prototype.modules = {}), t.name || "".concat(Object.keys(a.prototype.modules).length, "_").concat(K.now()));
							if ((a.prototype.modules[e] = t).proto && Object.keys(t.proto).forEach(function (e) {
									a.prototype[e] = t.proto[e]
								}), t.static && Object.keys(t.static).forEach(function (e) {
									a[e] = t.static[e]
								}), t.install) {
								for (var i = arguments.length, n = new Array(1 < i ? i - 1 : 0), s = 1; s < i; s++) n[s - 1] = arguments[s];
								t.install.apply(a, n)
							}
							return a
						}
					}, {
						key: "use",
						value: function (e) {
							var t = this;
							if (Array.isArray(e)) return e.forEach(function (e) {
								return t.installModule(e)
							}), t;
							for (var a = arguments.length, i = new Array(1 < a ? a - 1 : 0), n = 1; n < a; n++) i[n - 1] = arguments[n];
							return t.installModule.apply(t, [e].concat(i))
						}
					}]), a
				}();
			e = {
				updateSize: function () {
					var e = this,
						t = e.$el,
						a = void 0 !== e.params.width ? e.params.width : t[0].clientWidth,
						i = void 0 !== e.params.height ? e.params.height : t[0].clientHeight;
					0 === a && e.isHorizontal() || 0 === i && e.isVertical() || (a = a - parseInt(t.css("padding-left"), 10) - parseInt(t.css("padding-right"), 10), i = i - parseInt(t.css("padding-top"), 10) - parseInt(t.css("padding-bottom"), 10), K.extend(e, {
						width: a,
						height: i,
						size: e.isHorizontal() ? a : i
					}))
				},
				updateSlides: function () {
					var e = this,
						t = e.params,
						a = e.$wrapperEl,
						i = e.size,
						n = e.rtlTranslate,
						N = e.wrongRTL,
						H = ((h = e.virtual && t.virtual.enabled) ? e.virtual : e).slides.length,
						s = a.children(".".concat(e.params.slideClass)),
						r = (h ? e.virtual.slides : s).length,
						o = [],
						l = [],
						d = [];

					function c(e) {
						return !t.cssMode || e !== s.length - 1
					}
					var u = t.slidesOffsetBefore,
						p = ("function" == typeof u && (u = t.slidesOffsetBefore.call(e)), t.slidesOffsetAfter),
						h = ("function" == typeof p && (p = t.slidesOffsetAfter.call(e)), e.snapGrid.length),
						G = e.snapGrid.length,
						f = t.spaceBetween,
						m = -u,
						v = 0,
						g = 0;
					if (void 0 !== i) {
						"string" == typeof f && 0 <= f.indexOf("%") && (f = parseFloat(f.replace("%", "")) / 100 * i), e.virtualSize = -f, n ? s.css({
							marginLeft: "",
							marginTop: ""
						}) : s.css({
							marginRight: "",
							marginBottom: ""
						}), 1 < t.slidesPerColumn && (w = Math.floor(r / t.slidesPerColumn) === r / e.params.slidesPerColumn ? r : Math.ceil(r / t.slidesPerColumn) * t.slidesPerColumn, "auto" !== t.slidesPerView && "row" === t.slidesPerColumnFill && (w = Math.max(w, t.slidesPerView * t.slidesPerColumn)));
						for (var w, B, V, y, q, b = t.slidesPerColumn, F = w / b, X = Math.floor(r / t.slidesPerColumn), x = 0; x < r; x += 1) {
							k = 0;
							var T, E, S, C, M, P, k, $, z, L, Y, R, j, I = s.eq(x);
							1 < t.slidesPerColumn && (P = M = C = void 0, "row" === t.slidesPerColumnFill && 1 < t.slidesPerGroup ? (S = Math.floor(x / (t.slidesPerGroup * t.slidesPerColumn)), T = x - t.slidesPerColumn * t.slidesPerGroup * S, E = 0 === S ? t.slidesPerGroup : Math.min(Math.ceil((r - S * b * t.slidesPerGroup) / b), t.slidesPerGroup), M = T - (P = Math.floor(T / E)) * E + S * t.slidesPerGroup, I.css({
								"-webkit-box-ordinal-group": C = M + P * w / b,
								"-moz-box-ordinal-group": C,
								"-ms-flex-order": C,
								"-webkit-order": C,
								order: C
							})) : "column" === t.slidesPerColumnFill ? (P = x - (M = Math.floor(x / b)) * b, (X < M || M === X && P === b - 1) && b <= (P += 1) && (P = 0, M += 1)) : M = x - (P = Math.floor(x / F)) * F, I.css("margin-".concat(e.isHorizontal() ? "top" : "left"), 0 !== P && t.spaceBetween && "".concat(t.spaceBetween, "px"))), "none" !== I.css("display") && ("auto" === t.slidesPerView ? (T = U.window.getComputedStyle(I[0], null), E = I[0].style.transform, S = I[0].style.webkitTransform, E && (I[0].style.transform = "none"), S && (I[0].style.webkitTransform = "none"), k = t.roundLengths ? e.isHorizontal() ? I.outerWidth(!0) : I.outerHeight(!0) : e.isHorizontal() ? (C = parseFloat(T.getPropertyValue("width")), M = parseFloat(T.getPropertyValue("padding-left")), P = parseFloat(T.getPropertyValue("padding-right")), z = parseFloat(T.getPropertyValue("margin-left")), L = parseFloat(T.getPropertyValue("margin-right")), ($ = T.getPropertyValue("box-sizing")) && "border-box" === $ ? C + z + L : C + M + P + z + L) : ($ = parseFloat(T.getPropertyValue("height")), z = parseFloat(T.getPropertyValue("padding-top")), L = parseFloat(T.getPropertyValue("padding-bottom")), Y = parseFloat(T.getPropertyValue("margin-top")), R = parseFloat(T.getPropertyValue("margin-bottom")), (j = T.getPropertyValue("box-sizing")) && "border-box" === j ? $ + Y + R : $ + z + L + Y + R), E && (I[0].style.transform = E), S && (I[0].style.webkitTransform = S), t.roundLengths && (k = Math.floor(k))) : (k = (i - (t.slidesPerView - 1) * f) / t.slidesPerView, t.roundLengths && (k = Math.floor(k)), s[x] && (e.isHorizontal() ? s[x].style.width = "".concat(k, "px") : s[x].style.height = "".concat(k, "px"))), s[x] && (s[x].swiperSlideSize = k), d.push(k), t.centeredSlides ? (m = m + k / 2 + v / 2 + f, 0 === v && 0 !== x && (m = m - i / 2 - f), 0 === x && (m = m - i / 2 - f), Math.abs(m) < .001 && (m = 0), t.roundLengths && (m = Math.floor(m)), g % t.slidesPerGroup == 0 && o.push(m), l.push(m)) : (t.roundLengths && (m = Math.floor(m)), (g - Math.min(e.params.slidesPerGroupSkip, g)) % e.params.slidesPerGroup == 0 && o.push(m), l.push(m), m = m + k + f), e.virtualSize += k + f, v = k, g += 1)
						}
						if (e.virtualSize = Math.max(e.virtualSize, i) + p, n && N && ("slide" === t.effect || "coverflow" === t.effect) && a.css({
								width: "".concat(e.virtualSize + t.spaceBetween, "px")
							}), t.setWrapperSize && (e.isHorizontal() ? a.css({
								width: "".concat(e.virtualSize + t.spaceBetween, "px")
							}) : a.css({
								height: "".concat(e.virtualSize + t.spaceBetween, "px")
							})), 1 < t.slidesPerColumn && (e.virtualSize = (k + t.spaceBetween) * w, e.virtualSize = Math.ceil(e.virtualSize / t.slidesPerColumn) - t.spaceBetween, e.isHorizontal() ? a.css({
								width: "".concat(e.virtualSize + t.spaceBetween, "px")
							}) : a.css({
								height: "".concat(e.virtualSize + t.spaceBetween, "px")
							}), t.centeredSlides)) {
							for (var O = [], A = 0; A < o.length; A += 1) {
								var W = o[A];
								t.roundLengths && (W = Math.floor(W)), o[A] < e.virtualSize + o[0] && O.push(W)
							}
							o = O
						}
						if (!t.centeredSlides) {
							O = [];
							for (var D = 0; D < o.length; D += 1) {
								var _ = o[D];
								t.roundLengths && (_ = Math.floor(_)), o[D] <= e.virtualSize - i && O.push(_)
							}
							o = O, 1 < Math.floor(e.virtualSize - i) - Math.floor(o[o.length - 1]) && o.push(e.virtualSize - i)
						}
						0 === o.length && (o = [0]), 0 !== t.spaceBetween && (e.isHorizontal() ? n ? s.filter(c).css({
							marginLeft: "".concat(f, "px")
						}) : s.filter(c).css({
							marginRight: "".concat(f, "px")
						}) : s.filter(c).css({
							marginBottom: "".concat(f, "px")
						})), t.centeredSlides && t.centeredSlidesBounds && (B = 0, d.forEach(function (e) {
							B += e + (t.spaceBetween || 0)
						}), V = (B -= t.spaceBetween) - i, o = o.map(function (e) {
							return e < 0 ? -u : V < e ? V + p : e
						})), t.centerInsufficientSlides && (y = 0, d.forEach(function (e) {
							y += e + (t.spaceBetween || 0)
						}), (y -= t.spaceBetween) < i && (q = (i - y) / 2, o.forEach(function (e, t) {
							o[t] = e - q
						}), l.forEach(function (e, t) {
							l[t] = e + q
						}))), K.extend(e, {
							slides: s,
							snapGrid: o,
							slidesGrid: l,
							slidesSizesGrid: d
						}), r !== H && e.emit("slidesLengthChange"), o.length !== h && (e.params.watchOverflow && e.checkOverflow(), e.emit("snapGridLengthChange")), l.length !== G && e.emit("slidesGridLengthChange"), (t.watchSlidesProgress || t.watchSlidesVisibility) && e.updateSlidesOffset()
					}
				},
				updateAutoHeight: function (e) {
					var t, a, i = this,
						n = [],
						s = 0;
					if ("number" == typeof e ? i.setTransition(e) : !0 === e && i.setTransition(i.params.speed), "auto" !== i.params.slidesPerView && 1 < i.params.slidesPerView)
						if (i.params.centeredSlides) i.visibleSlides.each(function (e, t) {
							n.push(t)
						});
						else
							for (t = 0; t < Math.ceil(i.params.slidesPerView); t += 1) {
								var r = i.activeIndex + t;
								if (r > i.slides.length) break;
								n.push(i.slides.eq(r)[0])
							} else n.push(i.slides.eq(i.activeIndex)[0]);
					for (t = 0; t < n.length; t += 1) void 0 !== n[t] && (s = s < (a = n[t].offsetHeight) ? a : s);
					s && i.$wrapperEl.css("height", "".concat(s, "px"))
				},
				updateSlidesOffset: function () {
					for (var e = this.slides, t = 0; t < e.length; t += 1) e[t].swiperSlideOffset = this.isHorizontal() ? e[t].offsetLeft : e[t].offsetTop
				},
				updateSlidesProgress: function () {
					var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : this && this.translate || 0,
						t = this,
						a = t.params,
						i = t.slides,
						n = t.rtlTranslate;
					if (0 !== i.length) {
						void 0 === i[0].swiperSlideOffset && t.updateSlidesOffset();
						var s = n ? e : -e;
						i.removeClass(a.slideVisibleClass), t.visibleSlidesIndexes = [], t.visibleSlides = [];
						for (var r = 0; r < i.length; r += 1) {
							var o, l, d = i[r],
								c = (s + (a.centeredSlides ? t.minTranslate() : 0) - d.swiperSlideOffset) / (d.swiperSlideSize + a.spaceBetween);
							(a.watchSlidesVisibility || a.centeredSlides && a.autoHeight) && (l = (o = -(s - d.swiperSlideOffset)) + t.slidesSizesGrid[r], (0 <= o && o < t.size - 1 || 1 < l && l <= t.size || o <= 0 && l >= t.size) && (t.visibleSlides.push(d), t.visibleSlidesIndexes.push(r), i.eq(r).addClass(a.slideVisibleClass))), d.progress = n ? -c : c
						}
						t.visibleSlides = (0, E.$)(t.visibleSlides)
					}
				},
				updateProgress: function (e) {
					var t = this,
						a = (void 0 === e && (a = t.rtlTranslate ? -1 : 1, e = t && t.translate && t.translate * a || 0), t.params),
						i = t.maxTranslate() - t.minTranslate(),
						n = t.progress,
						s = t.isBeginning,
						r = s,
						o = l = t.isEnd,
						l = 0 == i ? s = !(n = 0) : (s = (n = (e - t.minTranslate()) / i) <= 0, 1 <= n);
					K.extend(t, {
						progress: n,
						isBeginning: s,
						isEnd: l
					}), (a.watchSlidesProgress || a.watchSlidesVisibility || a.centeredSlides && a.autoHeight) && t.updateSlidesProgress(e), s && !r && t.emit("reachBeginning toEdge"), l && !o && t.emit("reachEnd toEdge"), (r && !s || o && !l) && t.emit("fromEdge"), t.emit("progress", n)
				},
				updateSlidesClasses: function () {
					var e = (r = this).slides,
						t = r.params,
						a = r.$wrapperEl,
						i = r.activeIndex,
						n = r.realIndex,
						s = r.virtual && t.virtual.enabled,
						r = (e.removeClass("".concat(t.slideActiveClass, " ").concat(t.slideNextClass, " ").concat(t.slidePrevClass, " ").concat(t.slideDuplicateActiveClass, " ").concat(t.slideDuplicateNextClass, " ").concat(t.slideDuplicatePrevClass)), (s = s ? r.$wrapperEl.find(".".concat(t.slideClass, '[data-swiper-slide-index="').concat(i, '"]')) : e.eq(i)).addClass(t.slideActiveClass), t.loop && (s.hasClass(t.slideDuplicateClass) ? a.children(".".concat(t.slideClass, ":not(.").concat(t.slideDuplicateClass, ')[data-swiper-slide-index="').concat(n, '"]')) : a.children(".".concat(t.slideClass, ".").concat(t.slideDuplicateClass, '[data-swiper-slide-index="').concat(n, '"]'))).addClass(t.slideDuplicateActiveClass), s.nextAll(".".concat(t.slideClass)).eq(0).addClass(t.slideNextClass)),
						i = (t.loop && 0 === r.length && (r = e.eq(0)).addClass(t.slideNextClass), s.prevAll(".".concat(t.slideClass)).eq(0).addClass(t.slidePrevClass));
					t.loop && 0 === i.length && (i = e.eq(-1)).addClass(t.slidePrevClass), t.loop && ((r.hasClass(t.slideDuplicateClass) ? a.children(".".concat(t.slideClass, ":not(.").concat(t.slideDuplicateClass, ')[data-swiper-slide-index="').concat(r.attr("data-swiper-slide-index"), '"]')) : a.children(".".concat(t.slideClass, ".").concat(t.slideDuplicateClass, '[data-swiper-slide-index="').concat(r.attr("data-swiper-slide-index"), '"]'))).addClass(t.slideDuplicateNextClass), (i.hasClass(t.slideDuplicateClass) ? a.children(".".concat(t.slideClass, ":not(.").concat(t.slideDuplicateClass, ')[data-swiper-slide-index="').concat(i.attr("data-swiper-slide-index"), '"]')) : a.children(".".concat(t.slideClass, ".").concat(t.slideDuplicateClass, '[data-swiper-slide-index="').concat(i.attr("data-swiper-slide-index"), '"]'))).addClass(t.slideDuplicatePrevClass))
				},
				updateActiveIndex: function (e) {
					var t = this,
						a = t.rtlTranslate ? t.translate : -t.translate,
						i = t.slidesGrid,
						n = t.snapGrid,
						s = t.params,
						r = t.activeIndex,
						o = t.realIndex,
						l = t.snapIndex,
						d = e;
					if (void 0 === d) {
						for (var c = 0; c < i.length; c += 1) void 0 !== i[c + 1] ? a >= i[c] && a < i[c + 1] - (i[c + 1] - i[c]) / 2 ? d = c : a >= i[c] && a < i[c + 1] && (d = c + 1) : a >= i[c] && (d = c);
						s.normalizeSlideIndex && (d < 0 || void 0 === d) && (d = 0)
					}(e = 0 <= n.indexOf(a) ? n.indexOf(a) : (e = Math.min(s.slidesPerGroupSkip, d)) + Math.floor((d - e) / s.slidesPerGroup)) >= n.length && (e = n.length - 1), d === r ? e !== l && (t.snapIndex = e, t.emit("snapIndexChange")) : (s = parseInt(t.slides.eq(d).attr("data-swiper-slide-index") || d, 10), K.extend(t, {
						snapIndex: e,
						realIndex: s,
						previousIndex: r,
						activeIndex: d
					}), t.emit("activeIndexChange"), t.emit("snapIndexChange"), o !== s && t.emit("realIndexChange"), (t.initialized || t.params.runCallbacksOnInit) && t.emit("slideChange"))
				},
				updateClickedSlide: function (e) {
					var t = this,
						a = t.params,
						i = (0, E.$)(e.target).closest(".".concat(a.slideClass))[0],
						n = !1;
					if (i)
						for (var s = 0; s < t.slides.length; s += 1) t.slides[s] === i && (n = !0);
					i && n ? (t.clickedSlide = i, t.virtual && t.params.virtual.enabled ? t.clickedIndex = parseInt((0, E.$)(i).attr("data-swiper-slide-index"), 10) : t.clickedIndex = (0, E.$)(i).index(), a.slideToClickedSlide && void 0 !== t.clickedIndex && t.clickedIndex !== t.activeIndex && t.slideToClickedSlide()) : (t.clickedSlide = void 0, t.clickedIndex = void 0)
				}
			};
			t = {
				getTranslate: function () {
					var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : this.isHorizontal() ? "x" : "y",
						t = this.params,
						a = this.rtlTranslate,
						i = this.translate,
						n = this.$wrapperEl;
					return t.virtualTranslate ? a ? -i : i : t.cssMode ? i : (t = K.getTranslate(n[0], e), (t = a ? -t : t) || 0)
				},
				setTranslate: function (e, t) {
					var a = this,
						i = a.rtlTranslate,
						n = a.params,
						s = a.$wrapperEl,
						r = a.wrapperEl,
						o = a.progress,
						l = 0,
						d = 0;
					a.isHorizontal() ? l = i ? -e : e : d = e, n.roundLengths && (l = Math.floor(l), d = Math.floor(d)), n.cssMode ? r[a.isHorizontal() ? "scrollLeft" : "scrollTop"] = a.isHorizontal() ? -l : -d : n.virtualTranslate || s.transform("translate3d(".concat(l, "px, ").concat(d, "px, ").concat(0, "px)")), a.previousTranslate = a.translate, a.translate = a.isHorizontal() ? l : d, (r = 0 == (i = a.maxTranslate() - a.minTranslate()) ? 0 : (e - a.minTranslate()) / i) !== o && a.updateProgress(e), a.emit("setTranslate", a.translate, t)
				},
				minTranslate: function () {
					return -this.snapGrid[0]
				},
				maxTranslate: function () {
					return -this.snapGrid[this.snapGrid.length - 1]
				},
				translateTo: function () {
					var e, t, a = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : 0,
						i = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : this.params.speed,
						n = !(2 < arguments.length && void 0 !== arguments[2]) || arguments[2],
						s = !(3 < arguments.length && void 0 !== arguments[3]) || arguments[3],
						r = 4 < arguments.length ? arguments[4] : void 0,
						o = this,
						l = o.params,
						d = o.wrapperEl;
					return (!o.animating || !l.preventInteractionOnTransition) && (t = o.minTranslate(), e = o.maxTranslate(), o.updateProgress(t = s && t < a ? t : s && a < e ? e : a), l.cssMode ? (s = o.isHorizontal(), 0 !== i && d.scrollTo ? d.scrollTo((_defineProperty(e = {}, s ? "left" : "top", -t), _defineProperty(e, "behavior", "smooth"), e)) : d[s ? "scrollLeft" : "scrollTop"] = -t) : 0 === i ? (o.setTransition(0), o.setTranslate(t), n && (o.emit("beforeTransitionStart", i, r), o.emit("transitionEnd"))) : (o.setTransition(i), o.setTranslate(t), n && (o.emit("beforeTransitionStart", i, r), o.emit("transitionStart")), o.animating || (o.animating = !0, o.onTranslateToWrapperTransitionEnd || (o.onTranslateToWrapperTransitionEnd = function (e) {
						o && !o.destroyed && e.target === this && (o.$wrapperEl[0].removeEventListener("transitionend", o.onTranslateToWrapperTransitionEnd), o.$wrapperEl[0].removeEventListener("webkitTransitionEnd", o.onTranslateToWrapperTransitionEnd), o.onTranslateToWrapperTransitionEnd = null, delete o.onTranslateToWrapperTransitionEnd, n && o.emit("transitionEnd"))
					}), o.$wrapperEl[0].addEventListener("transitionend", o.onTranslateToWrapperTransitionEnd), o.$wrapperEl[0].addEventListener("webkitTransitionEnd", o.onTranslateToWrapperTransitionEnd))), !0)
				}
			};
			var H = {
				setTransition: function (e, t) {
					this.params.cssMode || this.$wrapperEl.transition(e), this.emit("setTransition", e, t)
				},
				transitionStart: function () {
					var e = !(0 < arguments.length && void 0 !== arguments[0]) || arguments[0],
						t = 1 < arguments.length ? arguments[1] : void 0,
						a = this,
						i = a.activeIndex,
						n = a.params,
						s = a.previousIndex;
					n.cssMode || (n.autoHeight && a.updateAutoHeight(), n = (n = t) || (s < i ? "next" : i < s ? "prev" : "reset"), a.emit("transitionStart"), e && i !== s && ("reset" === n ? a.emit("slideResetTransitionStart") : (a.emit("slideChangeTransitionStart"), "next" === n ? a.emit("slideNextTransitionStart") : a.emit("slidePrevTransitionStart"))))
				},
				transitionEnd: function () {
					var e = !(0 < arguments.length && void 0 !== arguments[0]) || arguments[0],
						t = 1 < arguments.length ? arguments[1] : void 0,
						a = this,
						i = a.activeIndex,
						n = a.previousIndex,
						s = a.params;
					a.animating = !1, s.cssMode || (a.setTransition(0), s = (s = t) || (n < i ? "next" : i < n ? "prev" : "reset"), a.emit("transitionEnd"), e && i !== n && ("reset" === s ? a.emit("slideResetTransitionEnd") : (a.emit("slideChangeTransitionEnd"), "next" === s ? a.emit("slideNextTransitionEnd") : a.emit("slidePrevTransitionEnd"))))
				}
			};
			var G = {
				slideTo: function () {
					var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : 0,
						t = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : this.params.speed,
						a = !(2 < arguments.length && void 0 !== arguments[2]) || arguments[2],
						i = 3 < arguments.length ? arguments[3] : void 0,
						n = this,
						s = e,
						e = (s < 0 && (s = 0), n.params),
						r = n.snapGrid,
						o = n.slidesGrid,
						l = n.previousIndex,
						d = n.activeIndex,
						c = n.rtlTranslate,
						u = n.wrapperEl;
					if (n.animating && e.preventInteractionOnTransition) return !1;
					var p, h = Math.min(n.params.slidesPerGroupSkip, s),
						f = ((h = h + Math.floor((s - h) / n.params.slidesPerGroup)) >= r.length && (h = r.length - 1), (d || e.initialSlide || 0) === (l || 0) && a && n.emit("beforeSlideChangeStart"), -r[h]);
					if (n.updateProgress(f), e.normalizeSlideIndex)
						for (var m = 0; m < o.length; m += 1) - Math.floor(100 * f) >= Math.floor(100 * o[m]) && (s = m);
					if (n.initialized && s !== d) {
						if (!n.allowSlideNext && f < n.translate && f < n.minTranslate()) return !1;
						if (!n.allowSlidePrev && f > n.translate && f > n.maxTranslate() && (d || 0) !== s) return !1
					}
					return p = d < s ? "next" : s < d ? "prev" : "reset", c && -f === n.translate || !c && f === n.translate ? (n.updateActiveIndex(s), e.autoHeight && n.updateAutoHeight(), n.updateSlidesClasses(), "slide" !== e.effect && n.setTranslate(f), "reset" !== p && (n.transitionStart(a, p), n.transitionEnd(a, p)), !1) : (e.cssMode ? (l = n.isHorizontal(), r = -f, c && (r = u.scrollWidth - u.offsetWidth - r), 0 !== t && u.scrollTo ? u.scrollTo((_defineProperty(h = {}, l ? "left" : "top", r), _defineProperty(h, "behavior", "smooth"), h)) : u[l ? "scrollLeft" : "scrollTop"] = r) : 0 === t ? (n.setTransition(0), n.setTranslate(f), n.updateActiveIndex(s), n.updateSlidesClasses(), n.emit("beforeTransitionStart", t, i), n.transitionStart(a, p), n.transitionEnd(a, p)) : (n.setTransition(t), n.setTranslate(f), n.updateActiveIndex(s), n.updateSlidesClasses(), n.emit("beforeTransitionStart", t, i), n.transitionStart(a, p), n.animating || (n.animating = !0, n.onSlideToWrapperTransitionEnd || (n.onSlideToWrapperTransitionEnd = function (e) {
						n && !n.destroyed && e.target === this && (n.$wrapperEl[0].removeEventListener("transitionend", n.onSlideToWrapperTransitionEnd), n.$wrapperEl[0].removeEventListener("webkitTransitionEnd", n.onSlideToWrapperTransitionEnd), n.onSlideToWrapperTransitionEnd = null, delete n.onSlideToWrapperTransitionEnd, n.transitionEnd(a, p))
					}), n.$wrapperEl[0].addEventListener("transitionend", n.onSlideToWrapperTransitionEnd), n.$wrapperEl[0].addEventListener("webkitTransitionEnd", n.onSlideToWrapperTransitionEnd))), !0)
				},
				slideToLoop: function () {
					var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : 0,
						t = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : this.params.speed;
					return this.params.loop && (e += this.loopedSlides), this.slideTo(e, t, !(2 < arguments.length && void 0 !== arguments[2]) || arguments[2], 3 < arguments.length ? arguments[3] : void 0)
				},
				slideNext: function () {
					var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : this.params.speed,
						t = !(1 < arguments.length && void 0 !== arguments[1]) || arguments[1],
						a = 2 < arguments.length ? arguments[2] : void 0,
						i = this,
						n = i.params,
						s = i.animating,
						r = i.activeIndex < n.slidesPerGroupSkip ? 1 : n.slidesPerGroup;
					if (n.loop) {
						if (s) return !1;
						i.loopFix(), i._clientLeft = i.$wrapperEl[0].clientLeft
					}
					return i.slideTo(i.activeIndex + r, e, t, a)
				},
				slidePrev: function () {
					var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : this.params.speed,
						t = !(1 < arguments.length && void 0 !== arguments[1]) || arguments[1],
						a = 2 < arguments.length ? arguments[2] : void 0,
						i = this,
						n = i.params,
						s = i.animating,
						r = i.snapGrid,
						o = i.slidesGrid,
						l = i.rtlTranslate;
					if (n.loop) {
						if (s) return !1;
						i.loopFix(), i._clientLeft = i.$wrapperEl[0].clientLeft
					}

					function d(e) {
						return e < 0 ? -Math.floor(Math.abs(e)) : Math.floor(e)
					}
					var c, u = d(l ? i.translate : -i.translate),
						s = r.map(d),
						p = (o.map(d), r[s.indexOf(u)], r[s.indexOf(u) - 1]);
					return void 0 === p && n.cssMode && r.forEach(function (e) {
						!p && e <= u && (p = e)
					}), void 0 !== p && (c = o.indexOf(p)) < 0 && (c = i.activeIndex - 1), i.slideTo(c, e, t, a)
				},
				slideReset: function () {
					var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : this.params.speed;
					return this.slideTo(this.activeIndex, e, !(1 < arguments.length && void 0 !== arguments[1]) || arguments[1], 2 < arguments.length ? arguments[2] : void 0)
				},
				slideToClosest: function () {
					var e, t = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : this.params.speed,
						a = !(1 < arguments.length && void 0 !== arguments[1]) || arguments[1],
						i = 2 < arguments.length ? arguments[2] : void 0,
						n = 3 < arguments.length && void 0 !== arguments[3] ? arguments[3] : .5,
						s = this,
						r = s.activeIndex,
						o = (o = Math.min(s.params.slidesPerGroupSkip, r)) + Math.floor((r - o) / s.params.slidesPerGroup),
						l = s.rtlTranslate ? s.translate : -s.translate;
					return l >= s.snapGrid[o] ? (e = s.snapGrid[o], (s.snapGrid[o + 1] - e) * n < l - e && (r += s.params.slidesPerGroup)) : l - (e = s.snapGrid[o - 1]) <= (s.snapGrid[o] - e) * n && (r -= s.params.slidesPerGroup), r = Math.max(r, 0), r = Math.min(r, s.slidesGrid.length - 1), s.slideTo(r, t, a, i)
				},
				slideToClickedSlide: function () {
					var e, t = this,
						a = t.params,
						i = t.$wrapperEl,
						n = "auto" === a.slidesPerView ? t.slidesPerViewDynamic() : a.slidesPerView,
						s = t.clickedIndex;
					a.loop ? t.animating || (e = parseInt((0, E.$)(t.clickedSlide).attr("data-swiper-slide-index"), 10), a.centeredSlides ? s < t.loopedSlides - n / 2 || s > t.slides.length - t.loopedSlides + n / 2 ? (t.loopFix(), s = i.children(".".concat(a.slideClass, '[data-swiper-slide-index="').concat(e, '"]:not(.').concat(a.slideDuplicateClass, ")")).eq(0).index(), K.nextTick(function () {
						t.slideTo(s)
					})) : t.slideTo(s) : s > t.slides.length - n ? (t.loopFix(), s = i.children(".".concat(a.slideClass, '[data-swiper-slide-index="').concat(e, '"]:not(.').concat(a.slideDuplicateClass, ")")).eq(0).index(), K.nextTick(function () {
						t.slideTo(s)
					})) : t.slideTo(s)) : t.slideTo(s)
				}
			};
			var B = {
				loopCreate: function () {
					var i = this,
						e = i.params,
						t = i.$wrapperEl,
						n = (t.children(".".concat(e.slideClass, ".").concat(e.slideDuplicateClass)).remove(), t.children(".".concat(e.slideClass)));
					if (e.loopFillGroupWithBlank) {
						var a = e.slidesPerGroup - n.length % e.slidesPerGroup;
						if (a !== e.slidesPerGroup) {
							for (var s = 0; s < a; s += 1) {
								var r = (0, E.$)(U.document.createElement("div")).addClass("".concat(e.slideClass, " ").concat(e.slideBlankClass));
								t.append(r)
							}
							n = t.children(".".concat(e.slideClass))
						}
					}
					"auto" !== e.slidesPerView || e.loopedSlides || (e.loopedSlides = n.length), i.loopedSlides = Math.ceil(parseFloat(e.loopedSlides || e.slidesPerView, 10)), i.loopedSlides += e.loopAdditionalSlides, i.loopedSlides > n.length && (i.loopedSlides = n.length);
					var o = [],
						l = [];
					n.each(function (e, t) {
						var a = (0, E.$)(t);
						e < i.loopedSlides && l.push(t), e < n.length && e >= n.length - i.loopedSlides && o.push(t), a.attr("data-swiper-slide-index", e)
					});
					for (var d = 0; d < l.length; d += 1) t.append((0, E.$)(l[d].cloneNode(!0)).addClass(e.slideDuplicateClass));
					for (var c = o.length - 1; 0 <= c; --c) t.prepend((0, E.$)(o[c].cloneNode(!0)).addClass(e.slideDuplicateClass))
				},
				loopFix: function () {
					var e = this,
						t = (e.emit("beforeLoopFix"), e.activeIndex),
						a = e.slides,
						i = e.loopedSlides,
						n = e.allowSlidePrev,
						s = e.allowSlideNext,
						r = e.snapGrid,
						o = e.rtlTranslate;
					e.allowSlidePrev = !0, e.allowSlideNext = !0;
					var l, r = -r[t] - e.getTranslate();
					t < i ? (l = a.length - 3 * i + t, e.slideTo(l += i, 0, !1, !0) && 0 != r && e.setTranslate((o ? -e.translate : e.translate) - r)) : t >= a.length - i && (l = -a.length + t + i, e.slideTo(l += i, 0, !1, !0) && 0 != r && e.setTranslate((o ? -e.translate : e.translate) - r)), e.allowSlidePrev = n, e.allowSlideNext = s, e.emit("loopFix")
				},
				loopDestroy: function () {
					var e = this.$wrapperEl,
						t = this.params,
						a = this.slides;
					e.children(".".concat(t.slideClass, ".").concat(t.slideDuplicateClass, ",.").concat(t.slideClass, ".").concat(t.slideBlankClass)).remove(), a.removeAttr("data-swiper-slide-index")
				}
			};
			var V = {
				setGrabCursor: function (e) {
					var t = this;
					b.touch || !t.params.simulateTouch || t.params.watchOverflow && t.isLocked || t.params.cssMode || ((t = t.el).style.cursor = "move", t.style.cursor = e ? "-webkit-grabbing" : "-webkit-grab", t.style.cursor = e ? "-moz-grabbin" : "-moz-grab", t.style.cursor = e ? "grabbing" : "grab")
				},
				unsetGrabCursor: function () {
					b.touch || this.params.watchOverflow && this.isLocked || this.params.cssMode || (this.el.style.cursor = "")
				}
			};
			var a, i, n, s, r, q = {
					appendSlide: function (e) {
						var t = this,
							a = t.$wrapperEl,
							i = t.params;
						if (i.loop && t.loopDestroy(), "object" === _typeof(e) && "length" in e)
							for (var n = 0; n < e.length; n += 1) e[n] && a.append(e[n]);
						else a.append(e);
						i.loop && t.loopCreate(), i.observer && b.observer || t.update()
					},
					prependSlide: function (e) {
						var t = this,
							a = t.params,
							i = t.$wrapperEl,
							n = t.activeIndex,
							s = (a.loop && t.loopDestroy(), n + 1);
						if ("object" === _typeof(e) && "length" in e) {
							for (var r = 0; r < e.length; r += 1) e[r] && i.prepend(e[r]);
							s = n + e.length
						} else i.prepend(e);
						a.loop && t.loopCreate(), a.observer && b.observer || t.update(), t.slideTo(s, 0, !1)
					},
					addSlide: function (e, t) {
						var a = this,
							i = a.$wrapperEl,
							n = a.params,
							s = a.activeIndex,
							r = (n.loop && (s -= a.loopedSlides, a.loopDestroy(), a.slides = i.children(".".concat(n.slideClass))), a.slides.length);
						if (e <= 0) a.prependSlide(t);
						else if (r <= e) a.appendSlide(t);
						else {
							for (var o = e < s ? s + 1 : s, l = [], d = r - 1; e <= d; --d) {
								var c = a.slides.eq(d);
								c.remove(), l.unshift(c)
							}
							if ("object" === _typeof(t) && "length" in t) {
								for (var u = 0; u < t.length; u += 1) t[u] && i.append(t[u]);
								o = e < s ? s + t.length : s
							} else i.append(t);
							for (var p = 0; p < l.length; p += 1) i.append(l[p]);
							n.loop && a.loopCreate(), n.observer && b.observer || a.update(), n.loop ? a.slideTo(o + a.loopedSlides, 0, !1) : a.slideTo(o, 0, !1)
						}
					},
					removeSlide: function (e) {
						var t, a = this,
							i = a.params,
							n = a.$wrapperEl,
							s = a.activeIndex,
							r = (i.loop && (s -= a.loopedSlides, a.loopDestroy(), a.slides = n.children(".".concat(i.slideClass))), s);
						if ("object" === _typeof(e) && "length" in e)
							for (var o = 0; o < e.length; o += 1) t = e[o], a.slides[t] && a.slides.eq(t).remove(), t < r && --r;
						else a.slides[t = e] && a.slides.eq(t).remove(), t < r && --r;
						r = Math.max(r, 0), i.loop && a.loopCreate(), i.observer && b.observer || a.update(), i.loop ? a.slideTo(r + a.loopedSlides, 0, !1) : a.slideTo(r, 0, !1)
					},
					removeAllSlides: function () {
						for (var e = [], t = 0; t < this.slides.length; t += 1) e.push(t);
						this.removeSlide(e)
					}
				},
				o = (a = U.window.navigator.platform, i = U.window.navigator.userAgent, n = {
					ios: !1,
					android: !1,
					androidChrome: !1,
					desktop: !1,
					iphone: !1,
					ipod: !1,
					ipad: !1,
					edge: !1,
					ie: !1,
					firefox: !1,
					macos: !1,
					windows: !1,
					cordova: !(!U.window.cordova && !U.window.phonegap),
					phonegap: !(!U.window.cordova && !U.window.phonegap),
					electron: !1
				}, u = U.window.screen.width, p = U.window.screen.height, w = i.match(/(Android);?[\s\/]+([\d.]+)?/), T = i.match(/(iPad).*OS\s([\d_]+)/), O = i.match(/(iPod)(.*OS\s([\d_]+))?/), x = !T && i.match(/(iPhone\sOS|iOS)\s([\d_]+)/), d = 0 <= i.indexOf("MSIE ") || 0 <= i.indexOf("Trident/"), m = 0 <= i.indexOf("Edge/"), v = 0 <= i.indexOf("Gecko/") && 0 <= i.indexOf("Firefox/"), s = "Win32" === a, r = 0 <= i.toLowerCase().indexOf("electron"), a = "MacIntel" === a, !T && a && b.touch && (1024 === u && 1366 === p || 834 === u && 1194 === p || 834 === u && 1112 === p || 768 === u && 1024 === p) && (T = i.match(/(Version)\/([\d.]+)/), a = !1), n.ie = d, n.edge = m, n.firefox = v, w && !s && (n.os = "android", n.osVersion = w[2], n.android = !0, n.androidChrome = 0 <= i.toLowerCase().indexOf("chrome")), (T || x || O) && (n.os = "ios", n.ios = !0), x && !O && (n.osVersion = x[2].replace(/_/g, "."), n.iphone = !0), T && (n.osVersion = T[2].replace(/_/g, "."), n.ipad = !0), O && (n.osVersion = O[3] ? O[3].replace(/_/g, ".") : null, n.ipod = !0), n.ios && n.osVersion && 0 <= i.indexOf("Version/") && "10" === n.osVersion.split(".")[0] && (n.osVersion = i.toLowerCase().split("version/")[1].split(" ")[0]), n.webView = !(!(x || T || O) || !i.match(/.*AppleWebKit(?!.*Safari)/i) && !U.window.navigator.standalone) || U.window.matchMedia && U.window.matchMedia("(display-mode: standalone)").matches, n.webview = n.webView, n.standalone = n.webView, n.desktop = !(n.ios || n.android) || r, n.desktop && (n.electron = r, n.macos = a, n.windows = s, n.macos && (n.os = "macos"), n.windows && (n.os = "windows")), n.pixelRatio = U.window.devicePixelRatio || 1, n);

			function l() {
				var e, t, a = this,
					i = a.params,
					n = a.el;
				n && 0 === n.offsetWidth || (i.breakpoints && a.setBreakpoint(), n = a.allowSlideNext, e = a.allowSlidePrev, t = a.snapGrid, a.allowSlideNext = !0, a.allowSlidePrev = !0, a.updateSize(), a.updateSlides(), a.updateSlidesClasses(), ("auto" === i.slidesPerView || 1 < i.slidesPerView) && a.isEnd && !a.params.centeredSlides ? a.slideTo(a.slides.length - 1, 0, !1, !0) : a.slideTo(a.activeIndex, 0, !1, !0), a.autoplay && a.autoplay.running && a.autoplay.paused && a.autoplay.run(), a.allowSlidePrev = e, a.allowSlideNext = n, a.params.watchOverflow && t !== a.snapGrid && a.checkOverflow())
			}
			var F = !1;

			function X() {}
			var d, Y = {
					init: !0,
					direction: "horizontal",
					touchEventsTarget: "container",
					initialSlide: 0,
					speed: 300,
					cssMode: !1,
					updateOnWindowResize: !0,
					preventInteractionOnTransition: !1,
					edgeSwipeDetection: !1,
					edgeSwipeThreshold: 20,
					freeMode: !1,
					freeModeMomentum: !0,
					freeModeMomentumRatio: 1,
					freeModeMomentumBounce: !0,
					freeModeMomentumBounceRatio: 1,
					freeModeMomentumVelocityRatio: 1,
					freeModeSticky: !1,
					freeModeMinimumVelocity: .02,
					autoHeight: !1,
					setWrapperSize: !1,
					virtualTranslate: !1,
					effect: "slide",
					breakpoints: void 0,
					spaceBetween: 0,
					slidesPerView: 1,
					slidesPerColumn: 1,
					slidesPerColumnFill: "column",
					slidesPerGroup: 1,
					slidesPerGroupSkip: 0,
					centeredSlides: !1,
					centeredSlidesBounds: !1,
					slidesOffsetBefore: 0,
					slidesOffsetAfter: 0,
					normalizeSlideIndex: !0,
					centerInsufficientSlides: !1,
					watchOverflow: !1,
					roundLengths: !1,
					touchRatio: 1,
					touchAngle: 45,
					simulateTouch: !0,
					shortSwipes: !0,
					longSwipes: !0,
					longSwipesRatio: .5,
					longSwipesMs: 300,
					followFinger: !0,
					allowTouchMove: !0,
					threshold: 0,
					touchMoveStopPropagation: !1,
					touchStartPreventDefault: !0,
					touchStartForcePreventDefault: !1,
					touchReleaseOnEdges: !1,
					uniqueNavElements: !0,
					resistance: !0,
					resistanceRatio: .85,
					watchSlidesProgress: !1,
					watchSlidesVisibility: !1,
					grabCursor: !1,
					preventClicks: !0,
					preventClicksPropagation: !0,
					slideToClickedSlide: !1,
					preloadImages: !0,
					updateOnImagesReady: !0,
					loop: !1,
					loopAdditionalSlides: 0,
					loopedSlides: null,
					loopFillGroupWithBlank: !1,
					allowSlidePrev: !0,
					allowSlideNext: !0,
					swipeHandler: null,
					noSwiping: !0,
					noSwipingClass: "swiper-no-swiping",
					noSwipingSelector: null,
					passiveListeners: !0,
					containerModifierClass: "swiper-container-",
					slideClass: "swiper-slide",
					slideBlankClass: "swiper-slide-invisible-blank",
					slideActiveClass: "swiper-slide-active",
					slideDuplicateActiveClass: "swiper-slide-duplicate-active",
					slideVisibleClass: "swiper-slide-visible",
					slideDuplicateClass: "swiper-slide-duplicate",
					slideNextClass: "swiper-slide-next",
					slideDuplicateNextClass: "swiper-slide-duplicate-next",
					slidePrevClass: "swiper-slide-prev",
					slideDuplicatePrevClass: "swiper-slide-duplicate-prev",
					wrapperClass: "swiper-wrapper",
					runCallbacksOnInit: !0
				},
				h = {
					update: e,
					translate: t,
					transition: H,
					slide: G,
					loop: B,
					grabCursor: V,
					manipulation: q,
					events: {
						attachEvents: function () {
							var e, t = this,
								a = t.params,
								i = t.touchEvents,
								n = t.el,
								s = t.wrapperEl,
								r = (t.onTouchStart = function (e) {
									var t, a, i, n, s, r = this,
										o = r.touchEventsData,
										l = r.params,
										d = r.touches;
									r.animating && l.preventInteractionOnTransition || ((e = e).originalEvent && (e = e.originalEvent), t = (0, E.$)(e.target), "wrapper" === l.touchEventsTarget && !t.closest(r.wrapperEl).length || (o.isTouchEvent = "touchstart" === e.type, !o.isTouchEvent && "which" in e && 3 === e.which || !o.isTouchEvent && "button" in e && 0 < e.button || o.isTouched && o.isMoved || (l.noSwiping && t.closest(l.noSwipingSelector || ".".concat(l.noSwipingClass))[0] ? r.allowClick = !0 : l.swipeHandler && !t.closest(l.swipeHandler)[0] || (d.currentX = ("touchstart" === e.type ? e.targetTouches[0] : e).pageX, d.currentY = ("touchstart" === e.type ? e.targetTouches[0] : e).pageY, a = d.currentX, i = d.currentY, n = l.edgeSwipeDetection || l.iOSEdgeSwipeDetection, s = l.edgeSwipeThreshold || l.iOSEdgeSwipeThreshold, n && (a <= s || a >= U.window.screen.width - s) || (K.extend(o, {
										isTouched: !0,
										isMoved: !1,
										allowTouchCallbacks: !0,
										isScrolling: void 0,
										startMoving: void 0
									}), d.startX = a, d.startY = i, o.touchStartTime = K.now(), r.allowClick = !0, r.updateSize(), r.swipeDirection = void 0, 0 < l.threshold && (o.allowThresholdMove = !1), "touchstart" !== e.type && (n = !0, t.is(o.formElements) && (n = !1), U.document.activeElement && (0, E.$)(U.document.activeElement).is(o.formElements) && U.document.activeElement !== t[0] && U.document.activeElement.blur(), s = n && r.allowTouchMove && l.touchStartPreventDefault, (l.touchStartForcePreventDefault || s) && e.preventDefault()), r.emit("touchStart", e))))))
								}.bind(t), t.onTouchMove = function (e) {
									var t = this,
										a = t.touchEventsData,
										i = t.params,
										n = t.touches,
										s = t.rtlTranslate;
									if (e.originalEvent && (e = e.originalEvent), a.isTouched) {
										if (!a.isTouchEvent || "mousemove" !== e.type) {
											var r = "touchmove" === e.type && e.targetTouches && (e.targetTouches[0] || e.changedTouches[0]),
												o = ("touchmove" === e.type ? r : e).pageX,
												r = ("touchmove" === e.type ? r : e).pageY;
											if (e.preventedByNestedSwiper) n.startX = o, n.startY = r;
											else if (t.allowTouchMove) {
												if (a.isTouchEvent && i.touchReleaseOnEdges && !i.loop)
													if (t.isVertical()) {
														if (r < n.startY && t.translate <= t.maxTranslate() || r > n.startY && t.translate >= t.minTranslate()) return a.isTouched = !1, void(a.isMoved = !1)
													} else if (o < n.startX && t.translate <= t.maxTranslate() || o > n.startX && t.translate >= t.minTranslate()) return;
												if (a.isTouchEvent && U.document.activeElement && e.target === U.document.activeElement && (0, E.$)(e.target).is(a.formElements)) a.isMoved = !0, t.allowClick = !1;
												else if (a.allowTouchCallbacks && t.emit("touchMove", e), !(e.targetTouches && 1 < e.targetTouches.length)) {
													n.currentX = o, n.currentY = r;
													var l = n.currentX - n.startX,
														d = n.currentY - n.startY;
													if (!(t.params.threshold && Math.sqrt(Math.pow(l, 2) + Math.pow(d, 2)) < t.params.threshold))
														if (void 0 === a.isScrolling && (t.isHorizontal() && n.currentY === n.startY || t.isVertical() && n.currentX === n.startX ? a.isScrolling = !1 : 25 <= l * l + d * d && (c = 180 * Math.atan2(Math.abs(d), Math.abs(l)) / Math.PI, a.isScrolling = t.isHorizontal() ? c > i.touchAngle : 90 - c > i.touchAngle)), a.isScrolling && t.emit("touchMoveOpposite", e), void 0 !== a.startMoving || n.currentX === n.startX && n.currentY === n.startY || (a.startMoving = !0), a.isScrolling) a.isTouched = !1;
														else if (a.startMoving) {
														t.allowClick = !1, i.cssMode || e.preventDefault(), i.touchMoveStopPropagation && !i.nested && e.stopPropagation(), a.isMoved || (i.loop && t.loopFix(), a.startTranslate = t.getTranslate(), t.setTransition(0), t.animating && t.$wrapperEl.trigger("webkitTransitionEnd transitionend"), a.allowMomentumBounce = !1, !i.grabCursor || !0 !== t.allowSlideNext && !0 !== t.allowSlidePrev || t.setGrabCursor(!0), t.emit("sliderFirstMove", e)), t.emit("sliderMove", e), a.isMoved = !0;
														var c = t.isHorizontal() ? l : d,
															l = (n.diff = c, c *= i.touchRatio, t.swipeDirection = 0 < (c = s ? -c : c) ? "prev" : "next", a.currentTranslate = c + a.startTranslate, !0),
															d = i.resistanceRatio;
														if (i.touchReleaseOnEdges && (d = 0), 0 < c && a.currentTranslate > t.minTranslate() ? (l = !1, i.resistance && (a.currentTranslate = t.minTranslate() - 1 + Math.pow(-t.minTranslate() + a.startTranslate + c, d))) : c < 0 && a.currentTranslate < t.maxTranslate() && (l = !1, i.resistance && (a.currentTranslate = t.maxTranslate() + 1 - Math.pow(t.maxTranslate() - a.startTranslate - c, d))), l && (e.preventedByNestedSwiper = !0), !t.allowSlideNext && "next" === t.swipeDirection && a.currentTranslate < a.startTranslate && (a.currentTranslate = a.startTranslate), !t.allowSlidePrev && "prev" === t.swipeDirection && a.currentTranslate > a.startTranslate && (a.currentTranslate = a.startTranslate), 0 < i.threshold) {
															if (!(Math.abs(c) > i.threshold || a.allowThresholdMove)) return void(a.currentTranslate = a.startTranslate);
															if (!a.allowThresholdMove) return a.allowThresholdMove = !0, n.startX = n.currentX, n.startY = n.currentY, a.currentTranslate = a.startTranslate, void(n.diff = t.isHorizontal() ? n.currentX - n.startX : n.currentY - n.startY)
														}
														i.followFinger && !i.cssMode && ((i.freeMode || i.watchSlidesProgress || i.watchSlidesVisibility) && (t.updateActiveIndex(), t.updateSlidesClasses()), i.freeMode && (0 === a.velocities.length && a.velocities.push({
															position: n[t.isHorizontal() ? "startX" : "startY"],
															time: a.touchStartTime
														}), a.velocities.push({
															position: n[t.isHorizontal() ? "currentX" : "currentY"],
															time: K.now()
														})), t.updateProgress(a.currentTranslate), t.setTranslate(a.currentTranslate))
													}
												}
											} else t.allowClick = !1, a.isTouched && (K.extend(n, {
												startX: o,
												startY: r,
												currentX: o,
												currentY: r
											}), a.touchStartTime = K.now())
										}
									} else a.startMoving && a.isScrolling && t.emit("touchMoveOpposite", e)
								}.bind(t), t.onTouchEnd = function (e) {
									var t = this,
										a = t.touchEventsData,
										i = t.params,
										n = t.touches,
										s = t.rtlTranslate,
										r = t.$wrapperEl,
										o = t.slidesGrid,
										l = t.snapGrid;
									if (e.originalEvent && (e = e.originalEvent), a.allowTouchCallbacks && t.emit("touchEnd", e), a.allowTouchCallbacks = !1, a.isTouched) {
										i.grabCursor && a.isMoved && a.isTouched && (!0 === t.allowSlideNext || !0 === t.allowSlidePrev) && t.setGrabCursor(!1);
										var d, c = K.now(),
											u = c - a.touchStartTime;
										if (t.allowClick && (t.updateClickedSlide(e), t.emit("tap click", e), u < 300 && c - a.lastClickTime < 300 && t.emit("doubleTap doubleClick", e)), a.lastClickTime = K.now(), K.nextTick(function () {
												t.destroyed || (t.allowClick = !0)
											}), a.isTouched && a.isMoved && t.swipeDirection && 0 !== n.diff && a.currentTranslate !== a.startTranslate) {
											if (a.isTouched = !1, a.isMoved = !1, a.startMoving = !1, d = i.followFinger ? s ? t.translate : -t.translate : -a.currentTranslate, !i.cssMode)
												if (i.freeMode)
													if (d < -t.minTranslate()) t.slideTo(t.activeIndex);
													else if (d > -t.maxTranslate()) t.slides.length < l.length ? t.slideTo(l.length - 1) : t.slideTo(t.slides.length - 1);
											else {
												if (i.freeModeMomentum) {
													1 < a.velocities.length && (c = a.velocities.pop(), n = a.velocities.pop(), f = c.position - n.position, n = c.time - n.time, t.velocity = f / n, t.velocity /= 2, Math.abs(t.velocity) < i.freeModeMinimumVelocity && (t.velocity = 0), !(150 < n || 300 < K.now() - c.time)) || (t.velocity = 0), t.velocity *= i.freeModeMomentumVelocityRatio, a.velocities.length = 0;
													var p, h, f = 1e3 * i.freeModeMomentumRatio,
														n = t.velocity * f,
														m = t.translate + n,
														c = (s && (m = -m), !1),
														n = 20 * Math.abs(t.velocity) * i.freeModeMomentumBounceRatio;
													if (m < t.maxTranslate()) i.freeModeMomentumBounce ? (m + t.maxTranslate() < -n && (m = t.maxTranslate() - n), p = t.maxTranslate(), a.allowMomentumBounce = c = !0) : m = t.maxTranslate(), i.loop && i.centeredSlides && (h = !0);
													else if (m > t.minTranslate()) i.freeModeMomentumBounce ? (m - t.minTranslate() > n && (m = t.minTranslate() + n), p = t.minTranslate(), a.allowMomentumBounce = c = !0) : m = t.minTranslate(), i.loop && i.centeredSlides && (h = !0);
													else if (i.freeModeSticky) {
														for (var v, g = 0; g < l.length; g += 1)
															if (l[g] > -m) {
																v = g;
																break
															} m = -(m = Math.abs(l[v] - m) < Math.abs(l[v - 1] - m) || "next" === t.swipeDirection ? l[v] : l[v - 1])
													}
													if (h && t.once("transitionEnd", function () {
															t.loopFix()
														}), 0 !== t.velocity) f = s ? Math.abs((-m - t.translate) / t.velocity) : Math.abs((m - t.translate) / t.velocity), i.freeModeSticky && (f = (n = Math.abs((s ? -m : m) - t.translate)) < (h = t.slidesSizesGrid[t.activeIndex]) ? i.speed : n < 2 * h ? 1.5 * i.speed : 2.5 * i.speed);
													else if (i.freeModeSticky) return void t.slideToClosest();
													i.freeModeMomentumBounce && c ? (t.updateProgress(p), t.setTransition(f), t.setTranslate(m), t.transitionStart(!0, t.swipeDirection), t.animating = !0, r.transitionEnd(function () {
														t && !t.destroyed && a.allowMomentumBounce && (t.emit("momentumBounce"), t.setTransition(i.speed), setTimeout(function () {
															t.setTranslate(p), r.transitionEnd(function () {
																t && !t.destroyed && t.transitionEnd()
															})
														}, 0))
													})) : t.velocity ? (t.updateProgress(m), t.setTransition(f), t.setTranslate(m), t.transitionStart(!0, t.swipeDirection), t.animating || (t.animating = !0, r.transitionEnd(function () {
														t && !t.destroyed && t.transitionEnd()
													}))) : t.updateProgress(m), t.updateActiveIndex(), t.updateSlidesClasses()
												} else if (i.freeModeSticky) return void t.slideToClosest();
												(!i.freeModeMomentum || u >= i.longSwipesMs) && (t.updateProgress(), t.updateActiveIndex(), t.updateSlidesClasses())
											} else {
												for (var w = 0, y = t.slidesSizesGrid[0], b = 0; b < o.length; b += b < i.slidesPerGroupSkip ? 1 : i.slidesPerGroup) {
													var x = b < i.slidesPerGroupSkip - 1 ? 1 : i.slidesPerGroup;
													void 0 !== o[b + x] ? d >= o[b] && d < o[b + x] && (y = o[(w = b) + x] - o[b]) : d >= o[b] && (w = b, y = o[o.length - 1] - o[o.length - 2])
												}
												s = (d - o[w]) / y, n = w < i.slidesPerGroupSkip - 1 ? 1 : i.slidesPerGroup;
												u > i.longSwipesMs ? i.longSwipes ? ("next" === t.swipeDirection && (s >= i.longSwipesRatio ? t.slideTo(w + n) : t.slideTo(w)), "prev" === t.swipeDirection && (s > 1 - i.longSwipesRatio ? t.slideTo(w + n) : t.slideTo(w))) : t.slideTo(t.activeIndex) : i.shortSwipes ? t.navigation && (e.target === t.navigation.nextEl || e.target === t.navigation.prevEl) ? e.target === t.navigation.nextEl ? t.slideTo(w + n) : t.slideTo(w) : ("next" === t.swipeDirection && t.slideTo(w + n), "prev" === t.swipeDirection && t.slideTo(w)) : t.slideTo(t.activeIndex)
											}
										} else a.isTouched = !1, a.isMoved = !1, a.startMoving = !1
									} else a.isMoved && i.grabCursor && t.setGrabCursor(!1), a.isMoved = !1, a.startMoving = !1
								}.bind(t), a.cssMode && (t.onScroll = function () {
									var e = this,
										t = e.wrapperEl,
										a = e.rtlTranslate;
									e.previousTranslate = e.translate, e.isHorizontal() ? e.translate = a ? t.scrollWidth - t.offsetWidth - t.scrollLeft : -t.scrollLeft : e.translate = -t.scrollTop, -0 === e.translate && (e.translate = 0), e.updateActiveIndex(), e.updateSlidesClasses(), (t = 0 == (t = e.maxTranslate() - e.minTranslate()) ? 0 : (e.translate - e.minTranslate()) / t) !== e.progress && e.updateProgress(a ? -e.translate : e.translate), e.emit("setTranslate", e.translate, !1)
								}.bind(t)), t.onClick = function (e) {
									this.allowClick || (this.params.preventClicks && e.preventDefault(), this.params.preventClicksPropagation && this.animating && (e.stopPropagation(), e.stopImmediatePropagation()))
								}.bind(t), !!a.nested);
							!b.touch && b.pointerEvents ? (n.addEventListener(i.start, t.onTouchStart, !1), U.document.addEventListener(i.move, t.onTouchMove, r), U.document.addEventListener(i.end, t.onTouchEnd, !1)) : (b.touch && (e = !("touchstart" !== i.start || !b.passiveListener || !a.passiveListeners) && {
								passive: !0,
								capture: !1
							}, n.addEventListener(i.start, t.onTouchStart, e), n.addEventListener(i.move, t.onTouchMove, b.passiveListener ? {
								passive: !1,
								capture: r
							} : r), n.addEventListener(i.end, t.onTouchEnd, e), i.cancel && n.addEventListener(i.cancel, t.onTouchEnd, e), F || (U.document.addEventListener("touchstart", X), F = !0)), (a.simulateTouch && !o.ios && !o.android || a.simulateTouch && !b.touch && o.ios) && (n.addEventListener("mousedown", t.onTouchStart, !1), U.document.addEventListener("mousemove", t.onTouchMove, r), U.document.addEventListener("mouseup", t.onTouchEnd, !1))), (a.preventClicks || a.preventClicksPropagation) && n.addEventListener("click", t.onClick, !0), a.cssMode && s.addEventListener("scroll", t.onScroll), a.updateOnWindowResize ? t.on(o.ios || o.android ? "resize orientationchange observerUpdate" : "resize observerUpdate", l, !0) : t.on("observerUpdate", l, !0)
						},
						detachEvents: function () {
							var e, t = this,
								a = t.params,
								i = t.touchEvents,
								n = t.el,
								s = t.wrapperEl,
								r = !!a.nested;
							!b.touch && b.pointerEvents ? (n.removeEventListener(i.start, t.onTouchStart, !1), U.document.removeEventListener(i.move, t.onTouchMove, r), U.document.removeEventListener(i.end, t.onTouchEnd, !1)) : (b.touch && (e = !("onTouchStart" !== i.start || !b.passiveListener || !a.passiveListeners) && {
								passive: !0,
								capture: !1
							}, n.removeEventListener(i.start, t.onTouchStart, e), n.removeEventListener(i.move, t.onTouchMove, r), n.removeEventListener(i.end, t.onTouchEnd, e), i.cancel && n.removeEventListener(i.cancel, t.onTouchEnd, e)), (a.simulateTouch && !o.ios && !o.android || a.simulateTouch && !b.touch && o.ios) && (n.removeEventListener("mousedown", t.onTouchStart, !1), U.document.removeEventListener("mousemove", t.onTouchMove, r), U.document.removeEventListener("mouseup", t.onTouchEnd, !1))), (a.preventClicks || a.preventClicksPropagation) && n.removeEventListener("click", t.onClick, !0), a.cssMode && s.removeEventListener("scroll", t.onScroll), t.off(o.ios || o.android ? "resize orientationchange observerUpdate" : "resize observerUpdate", l)
						}
					},
					breakpoints: {
						setBreakpoint: function () {
							var e, a, t, i, n = this,
								s = n.activeIndex,
								r = n.initialized,
								o = void 0 === (o = n.loopedSlides) ? 0 : o,
								l = n.params,
								d = n.$el,
								c = l.breakpoints;
							c && 0 !== Object.keys(c).length && (e = n.getBreakpoint(c)) && n.currentBreakpoint !== e && ((a = e in c ? c[e] : void 0) && ["slidesPerView", "spaceBetween", "slidesPerGroup", "slidesPerGroupSkip", "slidesPerColumn"].forEach(function (e) {
								var t = a[e];
								void 0 !== t && (a[e] = "slidesPerView" !== e || "AUTO" !== t && "auto" !== t ? "slidesPerView" === e ? parseFloat(t) : parseInt(t, 10) : "auto")
							}), c = a || n.originalParams, t = 1 < l.slidesPerColumn, i = 1 < c.slidesPerColumn, t && !i ? d.removeClass("".concat(l.containerModifierClass, "multirow ").concat(l.containerModifierClass, "multirow-column")) : !t && i && (d.addClass("".concat(l.containerModifierClass, "multirow")), "column" === c.slidesPerColumnFill && d.addClass("".concat(l.containerModifierClass, "multirow-column"))), t = c.direction && c.direction !== l.direction, i = l.loop && (c.slidesPerView !== l.slidesPerView || t), t && r && n.changeDirection(), K.extend(n.params, c), K.extend(n, {
								allowTouchMove: n.params.allowTouchMove,
								allowSlideNext: n.params.allowSlideNext,
								allowSlidePrev: n.params.allowSlidePrev
							}), n.currentBreakpoint = e, i && r && (n.loopDestroy(), n.loopCreate(), n.updateSlides(), n.slideTo(s - o + n.loopedSlides, 0, !1)), n.emit("breakpoint", c))
						},
						getBreakpoint: function (e) {
							if (e) {
								var t = !1,
									a = Object.keys(e).map(function (e) {
										var t;
										return "string" == typeof e && 0 === e.indexOf("@") ? (t = parseFloat(e.substr(1)), {
											value: U.window.innerHeight * t,
											point: e
										}) : {
											value: e,
											point: e
										}
									});
								a.sort(function (e, t) {
									return parseInt(e.value, 10) - parseInt(t.value, 10)
								});
								for (var i = 0; i < a.length; i += 1) {
									var n = a[i],
										s = n.point;
									n.value <= U.window.innerWidth && (t = s)
								}
								return t || "max"
							}
						}
					},
					checkOverflow: {
						checkOverflow: function () {
							var e = this,
								t = e.params,
								a = e.isLocked,
								i = 0 < e.slides.length && t.slidesOffsetBefore + t.spaceBetween * (e.slides.length - 1) + e.slides[0].offsetWidth * e.slides.length;
							t.slidesOffsetBefore && t.slidesOffsetAfter && i ? e.isLocked = i <= e.size : e.isLocked = 1 === e.snapGrid.length, e.allowSlideNext = !e.isLocked, e.allowSlidePrev = !e.isLocked, a !== e.isLocked && e.emit(e.isLocked ? "lock" : "unlock"), a && a !== e.isLocked && (e.isEnd = !1, e.navigation.update())
						}
					},
					classes: {
						addClasses: function () {
							var t = this.classNames,
								a = this.params,
								e = this.rtl,
								i = this.$el,
								n = [];
							n.push("initialized"), n.push(a.direction), a.freeMode && n.push("free-mode"), a.autoHeight && n.push("autoheight"), e && n.push("rtl"), 1 < a.slidesPerColumn && (n.push("multirow"), "column" === a.slidesPerColumnFill && n.push("multirow-column")), o.android && n.push("android"), o.ios && n.push("ios"), a.cssMode && n.push("css-mode"), n.forEach(function (e) {
								t.push(a.containerModifierClass + e)
							}), i.addClass(t.join(" "))
						},
						removeClasses: function () {
							var e = this.$el,
								t = this.classNames;
							e.removeClass(t.join(" "))
						}
					},
					images: {
						loadImage: function (e, t, a, i, n, s) {
							function r() {
								s && s()
							}(!e.complete || !n) && t ? ((e = new U.window.Image).onload = r, e.onerror = r, i && (e.sizes = i), a && (e.srcset = a), t && (e.src = t)) : r()
						},
						preloadImages: function () {
							var e = this;

							function t() {
								null != e && e && !e.destroyed && (void 0 !== e.imagesLoaded && (e.imagesLoaded += 1), e.imagesLoaded === e.imagesToLoad.length && (e.params.updateOnImagesReady && e.update(), e.emit("imagesReady")))
							}
							e.imagesToLoad = e.$el.find("img");
							for (var a = 0; a < e.imagesToLoad.length; a += 1) {
								var i = e.imagesToLoad[a];
								e.loadImage(i, i.currentSrc || i.getAttribute("src"), i.srcset || i.getAttribute("srcset"), i.sizes || i.getAttribute("sizes"), !0, t)
							}
						}
					}
				},
				f = {},
				c = function () {
					_inherits(p, N);
					var u = _createSuper(p);

					function p() {
						var e, a;
						_classCallCheck(this, p);
						for (var t = arguments.length, i = new Array(t), n = 0; n < t; n++) i[n] = arguments[n];
						a = (a = 1 === i.length && i[0].constructor && i[0].constructor === Object ? i[0] : (o = i[0], i[1])) || {}, a = K.extend({}, a), o && !a.el && (a.el = o), e = u.call(this, a), Object.keys(h).forEach(function (t) {
							Object.keys(h[t]).forEach(function (e) {
								p.prototype[e] || (p.prototype[e] = h[t][e])
							})
						});
						var s, r, o, l = _assertThisInitialized(e),
							d = (void 0 === l.modules && (l.modules = {}), Object.keys(l.modules).forEach(function (e) {
								var t, e = l.modules[e];
								e.params && (t = Object.keys(e.params)[0], "object" === _typeof(e = e.params[t]) && null !== e && t in a && "enabled" in e && (!0 === a[t] && (a[t] = {
									enabled: !0
								}), "object" !== _typeof(a[t]) || "enabled" in a[t] || (a[t].enabled = !0), a[t] || (a[t] = {
									enabled: !1
								})))
							}), K.extend({}, Y)),
							c = (l.useModulesParams(d), l.params = K.extend({}, d, f, a), l.originalParams = K.extend({}, l.params), l.passedParams = K.extend({}, a), l.$ = E.$, (0, E.$)(l.params.el));
						return (o = c[0]) ? 1 < c.length ? (s = [], c.each(function (e, t) {
							t = K.extend({}, a, {
								el: t
							});
							s.push(new p(t))
						}), _possibleConstructorReturn(e, s)) : (o.swiper = l, c.data("swiper", l), o && o.shadowRoot && o.shadowRoot.querySelector ? (r = (0, E.$)(o.shadowRoot.querySelector(".".concat(l.params.wrapperClass)))).children = function (e) {
							return c.children(e)
						} : r = c.children(".".concat(l.params.wrapperClass)), K.extend(l, {
							$el: c,
							el: o,
							$wrapperEl: r,
							wrapperEl: r[0],
							classNames: [],
							slides: (0, E.$)(),
							slidesGrid: [],
							snapGrid: [],
							slidesSizesGrid: [],
							isHorizontal: function () {
								return "horizontal" === l.params.direction
							},
							isVertical: function () {
								return "vertical" === l.params.direction
							},
							rtl: "rtl" === o.dir.toLowerCase() || "rtl" === c.css("direction"),
							rtlTranslate: "horizontal" === l.params.direction && ("rtl" === o.dir.toLowerCase() || "rtl" === c.css("direction")),
							wrongRTL: "-webkit-box" === r.css("display"),
							activeIndex: 0,
							realIndex: 0,
							isBeginning: !0,
							isEnd: !1,
							translate: 0,
							previousTranslate: 0,
							progress: 0,
							velocity: 0,
							animating: !1,
							allowSlideNext: l.params.allowSlideNext,
							allowSlidePrev: l.params.allowSlidePrev,
							touchEvents: (d = ["touchstart", "touchmove", "touchend", "touchcancel"], o = b.pointerEvents ? ["pointerdown", "pointermove", "pointerup"] : ["mousedown", "mousemove", "mouseup"], l.touchEventsTouch = {
								start: d[0],
								move: d[1],
								end: d[2],
								cancel: d[3]
							}, l.touchEventsDesktop = {
								start: o[0],
								move: o[1],
								end: o[2]
							}, b.touch || !l.params.simulateTouch ? l.touchEventsTouch : l.touchEventsDesktop),
							touchEventsData: {
								isTouched: void 0,
								isMoved: void 0,
								allowTouchCallbacks: void 0,
								touchStartTime: void 0,
								isScrolling: void 0,
								currentTranslate: void 0,
								startTranslate: void 0,
								allowThresholdMove: void 0,
								formElements: "input, select, option, textarea, button, video, label",
								lastClickTime: K.now(),
								clickTimeout: void 0,
								velocities: [],
								allowMomentumBounce: void 0,
								isTouchEvent: void 0,
								startMoving: void 0
							},
							allowClick: !0,
							allowTouchMove: l.params.allowTouchMove,
							touches: {
								startX: 0,
								startY: 0,
								currentX: 0,
								currentY: 0,
								diff: 0
							},
							imagesToLoad: [],
							imagesLoaded: 0
						}), l.useModules(), l.params.init && l.init(), _possibleConstructorReturn(e, l)) : _possibleConstructorReturn(e, void 0)
					}
					return _createClass(p, [{
						key: "slidesPerViewDynamic",
						value: function () {
							var e = this,
								t = e.params,
								a = e.slides,
								i = e.slidesGrid,
								n = e.size,
								s = e.activeIndex,
								r = 1;
							if (t.centeredSlides) {
								for (var o, l = a[s].swiperSlideSize, d = s + 1; d < a.length; d += 1) a[d] && !o && (r += 1, n < (l += a[d].swiperSlideSize) && (o = !0));
								for (var c = s - 1; 0 <= c; --c) a[c] && !o && (r += 1, n < (l += a[c].swiperSlideSize) && (o = !0))
							} else
								for (var u = s + 1; u < a.length; u += 1) i[u] - i[s] < n && (r += 1);
							return r
						}
					}, {
						key: "update",
						value: function () {
							var e, t, a = this;

							function i() {
								var e = a.rtlTranslate ? -1 * a.translate : a.translate,
									e = Math.min(Math.max(e, a.maxTranslate()), a.minTranslate());
								a.setTranslate(e), a.updateActiveIndex(), a.updateSlidesClasses()
							}
							a && !a.destroyed && (e = a.snapGrid, (t = a.params).breakpoints && a.setBreakpoint(), a.updateSize(), a.updateSlides(), a.updateProgress(), a.updateSlidesClasses(), a.params.freeMode ? (i(), a.params.autoHeight && a.updateAutoHeight()) : (("auto" === a.params.slidesPerView || 1 < a.params.slidesPerView) && a.isEnd && !a.params.centeredSlides ? a.slideTo(a.slides.length - 1, 0, !1, !0) : a.slideTo(a.activeIndex, 0, !1, !0)) || i(), t.watchOverflow && e !== a.snapGrid && a.checkOverflow(), a.emit("update"))
						}
					}, {
						key: "changeDirection",
						value: function (a) {
							var e = !(1 < arguments.length && void 0 !== arguments[1]) || arguments[1],
								t = this,
								i = t.params.direction;
							return (a = a || ("horizontal" === i ? "vertical" : "horizontal")) === i || "horizontal" !== a && "vertical" !== a || (t.$el.removeClass("".concat(t.params.containerModifierClass).concat(i)).addClass("".concat(t.params.containerModifierClass).concat(a)), t.params.direction = a, t.slides.each(function (e, t) {
								"vertical" === a ? t.style.width = "" : t.style.height = ""
							}), t.emit("changeDirection"), e && t.update()), t
						}
					}, {
						key: "init",
						value: function () {
							var e = this;
							e.initialized || (e.emit("beforeInit"), e.params.breakpoints && e.setBreakpoint(), e.addClasses(), e.params.loop && e.loopCreate(), e.updateSize(), e.updateSlides(), e.params.watchOverflow && e.checkOverflow(), e.params.grabCursor && e.setGrabCursor(), e.params.preloadImages && e.preloadImages(), e.params.loop ? e.slideTo(e.params.initialSlide + e.loopedSlides, 0, e.params.runCallbacksOnInit) : e.slideTo(e.params.initialSlide, 0, e.params.runCallbacksOnInit), e.attachEvents(), e.initialized = !0, e.emit("init"))
						}
					}, {
						key: "destroy",
						value: function () {
							var e = !(0 < arguments.length && void 0 !== arguments[0]) || arguments[0],
								t = !(1 < arguments.length && void 0 !== arguments[1]) || arguments[1],
								a = this,
								i = a.params,
								n = a.$el,
								s = a.$wrapperEl,
								r = a.slides;
							return void 0 === a.params || a.destroyed || (a.emit("beforeDestroy"), a.initialized = !1, a.detachEvents(), i.loop && a.loopDestroy(), t && (a.removeClasses(), n.removeAttr("style"), s.removeAttr("style"), r && r.length && r.removeClass([i.slideVisibleClass, i.slideActiveClass, i.slideNextClass, i.slidePrevClass].join(" ")).removeAttr("style").removeAttr("data-swiper-slide-index")), a.emit("destroy"), Object.keys(a.eventsListeners).forEach(function (e) {
								a.off(e)
							}), !1 !== e && (a.$el[0].swiper = null, a.$el.data("swiper", null), K.deleteProps(a)), a.destroyed = !0), null
						}
					}], [{
						key: "extendDefaults",
						value: function (e) {
							K.extend(f, e)
						}
					}, {
						key: "extendedDefaults",
						get: function () {
							return f
						}
					}, {
						key: "defaults",
						get: function () {
							return Y
						}
					}, {
						key: "Class",
						get: function () {
							return N
						}
					}, {
						key: "$",
						get: function () {
							return E.$
						}
					}]), p
				}(),
				u = {
					name: "device",
					proto: {
						device: o
					},
					static: {
						device: o
					}
				},
				p = {
					name: "support",
					proto: {
						support: b
					},
					static: {
						support: b
					}
				},
				S = {
					isEdge: !!U.window.navigator.userAgent.match(/Edge/g),
					isSafari: 0 <= (d = U.window.navigator.userAgent.toLowerCase()).indexOf("safari") && d.indexOf("chrome") < 0 && d.indexOf("android") < 0,
					isUiWebView: /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(U.window.navigator.userAgent)
				},
				m = {
					name: "browser",
					proto: {
						browser: S
					},
					static: {
						browser: S
					}
				},
				v = {
					name: "resize",
					create: function () {
						var e = this;
						K.extend(e, {
							resize: {
								resizeHandler: function () {
									e && !e.destroyed && e.initialized && (e.emit("beforeResize"), e.emit("resize"))
								},
								orientationChangeHandler: function () {
									e && !e.destroyed && e.initialized && e.emit("orientationchange")
								}
							}
						})
					},
					on: {
						init: function () {
							U.window.addEventListener("resize", this.resize.resizeHandler), U.window.addEventListener("orientationchange", this.resize.orientationChangeHandler)
						},
						destroy: function () {
							U.window.removeEventListener("resize", this.resize.resizeHandler), U.window.removeEventListener("orientationchange", this.resize.orientationChangeHandler)
						}
					}
				},
				g = {
					func: U.window.MutationObserver || U.window.WebkitMutationObserver,
					attach: function (e) {
						var t = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : {},
							a = this,
							i = new g.func(function (e) {
								var t;
								1 === e.length ? a.emit("observerUpdate", e[0]) : (t = function () {
									a.emit("observerUpdate", e[0])
								}, U.window.requestAnimationFrame ? U.window.requestAnimationFrame(t) : U.window.setTimeout(t, 0))
							});
						i.observe(e, {
							attributes: void 0 === t.attributes || t.attributes,
							childList: void 0 === t.childList || t.childList,
							characterData: void 0 === t.characterData || t.characterData
						}), a.observer.observers.push(i)
					},
					init: function () {
						var e = this;
						if (b.observer && e.params.observer) {
							if (e.params.observeParents)
								for (var t = e.$el.parents(), a = 0; a < t.length; a += 1) e.observer.attach(t[a]);
							e.observer.attach(e.$el[0], {
								childList: e.params.observeSlideChildren
							}), e.observer.attach(e.$wrapperEl[0], {
								attributes: !1
							})
						}
					},
					destroy: function () {
						this.observer.observers.forEach(function (e) {
							e.disconnect()
						}), this.observer.observers = []
					}
				},
				w = {
					name: "observer",
					params: {
						observer: !1,
						observeParents: !1,
						observeSlideChildren: !1
					},
					create: function () {
						K.extend(this, {
							observer: {
								init: g.init.bind(this),
								attach: g.attach.bind(this),
								destroy: g.destroy.bind(this),
								observers: []
							}
						})
					},
					on: {
						init: function () {
							this.observer.init()
						},
						destroy: function () {
							this.observer.destroy()
						}
					}
				},
				y = {
					update: function (e) {
						var t, a = this,
							i = a.params,
							n = i.slidesPerView,
							s = i.slidesPerGroup,
							i = i.centeredSlides,
							r = a.params.virtual,
							o = r.addSlidesBefore,
							r = r.addSlidesAfter,
							l = a.virtual,
							d = l.from,
							c = l.to,
							u = l.slides,
							p = l.slidesGrid,
							h = l.renderSlide,
							l = l.offset,
							f = (a.updateActiveIndex(), a.activeIndex || 0),
							m = a.rtlTranslate ? "right" : a.isHorizontal() ? "left" : "top",
							i = i ? (t = Math.floor(n / 2) + s + o, Math.floor(n / 2) + s + r) : (t = n + (s - 1) + o, s + r),
							v = Math.max((f || 0) - i, 0),
							g = Math.min((f || 0) + t, u.length - 1),
							n = (a.slidesGrid[v] || 0) - (a.slidesGrid[0] || 0);

						function w() {
							a.updateSlides(), a.updateProgress(), a.updateSlidesClasses(), a.lazy && a.params.lazy.enabled && a.lazy.load()
						}
						if (K.extend(a.virtual, {
								from: v,
								to: g,
								offset: n,
								slidesGrid: a.slidesGrid
							}), d !== v || c !== g || e) {
							if (a.params.virtual.renderExternal) a.params.virtual.renderExternal.call(a, {
								offset: n,
								from: v,
								to: g,
								slides: function () {
									for (var e = [], t = v; t <= g; t += 1) e.push(u[t]);
									return e
								}()
							});
							else {
								var y = [],
									b = [];
								if (e) a.$wrapperEl.find(".".concat(a.params.slideClass)).remove();
								else
									for (var x = d; x <= c; x += 1)(x < v || g < x) && a.$wrapperEl.find(".".concat(a.params.slideClass, '[data-swiper-slide-index="').concat(x, '"]')).remove();
								for (var T = 0; T < u.length; T += 1) v <= T && T <= g && (void 0 === c || e ? b.push(T) : (c < T && b.push(T), T < d && y.push(T)));
								b.forEach(function (e) {
									a.$wrapperEl.append(h(u[e], e))
								}), y.sort(function (e, t) {
									return t - e
								}).forEach(function (e) {
									a.$wrapperEl.prepend(h(u[e], e))
								}), a.$wrapperEl.children(".swiper-slide").css(m, "".concat(n, "px"))
							}
							w()
						} else a.slidesGrid !== p && n !== l && a.slides.css(m, "".concat(n, "px")), a.updateProgress()
					},
					renderSlide: function (e, t) {
						var a = this,
							i = a.params.virtual;
						return i.cache && a.virtual.cache[t] ? a.virtual.cache[t] : ((e = i.renderSlide ? (0, E.$)(i.renderSlide.call(a, e, t)) : (0, E.$)('<div class="'.concat(a.params.slideClass, '" data-swiper-slide-index="').concat(t, '">').concat(e, "</div>"))).attr("data-swiper-slide-index") || e.attr("data-swiper-slide-index", t), i.cache && (a.virtual.cache[t] = e), e)
					},
					appendSlide: function (e) {
						if ("object" === _typeof(e) && "length" in e)
							for (var t = 0; t < e.length; t += 1) e[t] && this.virtual.slides.push(e[t]);
						else this.virtual.slides.push(e);
						this.virtual.update(!0)
					},
					prependSlide: function (e) {
						var i, n, t = this,
							a = t.activeIndex,
							s = a + 1,
							r = 1;
						if (Array.isArray(e)) {
							for (var o = 0; o < e.length; o += 1) e[o] && t.virtual.slides.unshift(e[o]);
							s = a + e.length, r = e.length
						} else t.virtual.slides.unshift(e);
						t.params.virtual.cache && (i = t.virtual.cache, n = {}, Object.keys(i).forEach(function (e) {
							var t = i[e],
								a = t.attr("data-swiper-slide-index");
							a && t.attr("data-swiper-slide-index", parseInt(a, 10) + 1), n[parseInt(e, 10) + r] = t
						}), t.virtual.cache = n), t.virtual.update(!0), t.slideTo(s, 0)
					},
					removeSlide: function (e) {
						var t = this;
						if (null != e) {
							var a = t.activeIndex;
							if (Array.isArray(e))
								for (var i = e.length - 1; 0 <= i; --i) t.virtual.slides.splice(e[i], 1), t.params.virtual.cache && delete t.virtual.cache[e[i]], e[i] < a && --a, a = Math.max(a, 0);
							else t.virtual.slides.splice(e, 1), t.params.virtual.cache && delete t.virtual.cache[e], e < a && --a, a = Math.max(a, 0);
							t.virtual.update(!0), t.slideTo(a, 0)
						}
					},
					removeAllSlides: function () {
						var e = this;
						e.virtual.slides = [], e.params.virtual.cache && (e.virtual.cache = {}), e.virtual.update(!0), e.slideTo(0, 0)
					}
				},
				x = {
					name: "virtual",
					params: {
						virtual: {
							enabled: !1,
							slides: [],
							cache: !0,
							renderSlide: null,
							renderExternal: null,
							addSlidesBefore: 0,
							addSlidesAfter: 0
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							virtual: {
								update: y.update.bind(e),
								appendSlide: y.appendSlide.bind(e),
								prependSlide: y.prependSlide.bind(e),
								removeSlide: y.removeSlide.bind(e),
								removeAllSlides: y.removeAllSlides.bind(e),
								renderSlide: y.renderSlide.bind(e),
								slides: e.params.virtual.slides,
								cache: {}
							}
						})
					},
					on: {
						beforeInit: function () {
							var e, t = this;
							t.params.virtual.enabled && (t.classNames.push("".concat(t.params.containerModifierClass, "virtual")), K.extend(t.params, e = {
								watchSlidesProgress: !0
							}), K.extend(t.originalParams, e), t.params.initialSlide || t.virtual.update())
						},
						setTranslate: function () {
							this.params.virtual.enabled && this.virtual.update()
						}
					}
				},
				R = {
					handle: function (e) {
						var t = this,
							a = t.rtlTranslate,
							i = (e = e.originalEvent ? e.originalEvent : e).keyCode || e.charCode;
						if (!t.allowSlideNext && (t.isHorizontal() && 39 === i || t.isVertical() && 40 === i || 34 === i)) return !1;
						if (!t.allowSlidePrev && (t.isHorizontal() && 37 === i || t.isVertical() && 38 === i || 33 === i)) return !1;
						if (!(e.shiftKey || e.altKey || e.ctrlKey || e.metaKey || U.document.activeElement && U.document.activeElement.nodeName && ("input" === U.document.activeElement.nodeName.toLowerCase() || "textarea" === U.document.activeElement.nodeName.toLowerCase()))) {
							if (t.params.keyboard.onlyInViewport && (33 === i || 34 === i || 37 === i || 39 === i || 38 === i || 40 === i)) {
								var n = !1;
								if (0 < t.$el.parents(".".concat(t.params.slideClass)).length && 0 === t.$el.parents(".".concat(t.params.slideActiveClass)).length) return;
								for (var s = U.window.innerWidth, r = U.window.innerHeight, o = t.$el.offset(), l = (a && (o.left -= t.$el[0].scrollLeft), [
										[o.left, o.top],
										[o.left + t.width, o.top],
										[o.left, o.top + t.height],
										[o.left + t.width, o.top + t.height]
									]), d = 0; d < l.length; d += 1) {
									var c = l[d];
									0 <= c[0] && c[0] <= s && 0 <= c[1] && c[1] <= r && (n = !0)
								}
								if (!n) return
							}
							t.isHorizontal() ? (33 !== i && 34 !== i && 37 !== i && 39 !== i || (e.preventDefault ? e.preventDefault() : e.returnValue = !1), (34 !== i && 39 !== i || a) && (33 !== i && 37 !== i || !a) || t.slideNext(), (33 !== i && 37 !== i || a) && (34 !== i && 39 !== i || !a) || t.slidePrev()) : (33 !== i && 34 !== i && 38 !== i && 40 !== i || (e.preventDefault ? e.preventDefault() : e.returnValue = !1), 34 !== i && 40 !== i || t.slideNext(), 33 !== i && 38 !== i || t.slidePrev()), t.emit("keyPress", i)
						}
					},
					enable: function () {
						this.keyboard.enabled || ((0, E.$)(U.document).on("keydown", this.keyboard.handle), this.keyboard.enabled = !0)
					},
					disable: function () {
						this.keyboard.enabled && ((0, E.$)(U.document).off("keydown", this.keyboard.handle), this.keyboard.enabled = !1)
					}
				},
				T = {
					name: "keyboard",
					params: {
						keyboard: {
							enabled: !1,
							onlyInViewport: !0
						}
					},
					create: function () {
						K.extend(this, {
							keyboard: {
								enabled: !1,
								enable: R.enable.bind(this),
								disable: R.disable.bind(this),
								handle: R.handle.bind(this)
							}
						})
					},
					on: {
						init: function () {
							this.params.keyboard.enabled && this.keyboard.enable()
						},
						destroy: function () {
							this.keyboard.enabled && this.keyboard.disable()
						}
					}
				};
			var C = {
					lastScrollTime: K.now(),
					lastEventBeforeSnap: void 0,
					recentWheelEvents: [],
					event: function () {
						return -1 < U.window.navigator.userAgent.indexOf("firefox") ? "DOMMouseScroll" : ((a = (t = "onwheel") in U.document) || ((e = U.document.createElement("div")).setAttribute(t, "return;"), a = "function" == typeof e[t]), (a = !a && U.document.implementation && U.document.implementation.hasFeature && !0 !== U.document.implementation.hasFeature("", "") ? U.document.implementation.hasFeature("Events.wheel", "3.0") : a) ? "wheel" : "mousewheel");
						var e, t, a
					},
					normalize: function (e) {
						var t = 0,
							a = 0,
							i = 0,
							n = 0;
						return "detail" in e && (a = e.detail), "wheelDelta" in e && (a = -e.wheelDelta / 120), "wheelDeltaY" in e && (a = -e.wheelDeltaY / 120), "wheelDeltaX" in e && (t = -e.wheelDeltaX / 120), "axis" in e && e.axis === e.HORIZONTAL_AXIS && (t = a, a = 0), i = 10 * t, n = 10 * a, "deltaY" in e && (n = e.deltaY), "deltaX" in e && (i = e.deltaX), e.shiftKey && !i && (i = n, n = 0), (i || n) && e.deltaMode && (1 === e.deltaMode ? (i *= 40, n *= 40) : (i *= 800, n *= 800)), {
							spinX: t = i && !t ? i < 1 ? -1 : 1 : t,
							spinY: a = n && !a ? n < 1 ? -1 : 1 : a,
							pixelX: i,
							pixelY: n
						}
					},
					handleMouseEnter: function () {
						this.mouseEntered = !0
					},
					handleMouseLeave: function () {
						this.mouseEntered = !1
					},
					handle: function (e) {
						var t = e,
							a = this,
							i = a.params.mousewheel,
							n = (a.params.cssMode && t.preventDefault(), a.$el);
						if ("container" !== a.params.mousewheel.eventsTarged && (n = (0, E.$)(a.params.mousewheel.eventsTarged)), !a.mouseEntered && !n[0].contains(t.target) && !i.releaseOnEdges) return !0;
						t.originalEvent && (t = t.originalEvent);
						var n = 0,
							s = a.rtlTranslate ? -1 : 1,
							r = C.normalize(t);
						if (i.forceToAxis)
							if (a.isHorizontal()) {
								if (!(Math.abs(r.pixelX) > Math.abs(r.pixelY))) return !0;
								n = r.pixelX * s
							} else {
								if (!(Math.abs(r.pixelY) > Math.abs(r.pixelX))) return !0;
								n = r.pixelY
							}
						else n = Math.abs(r.pixelX) > Math.abs(r.pixelY) ? -r.pixelX * s : -r.pixelY;
						if (0 === n) return !0;
						if (i.invert && (n = -n), a.params.freeMode) {
							var o = {
									time: K.now(),
									delta: Math.abs(n),
									direction: Math.sign(n)
								},
								s = a.mousewheel.lastEventBeforeSnap,
								r = s && o.time < s.time + 500 && o.delta <= s.delta && o.direction === s.direction;
							if (!r) {
								a.mousewheel.lastEventBeforeSnap = void 0, a.params.loop && a.loopFix();
								var l, d, s = a.getTranslate() + n * i.sensitivity,
									i = a.isBeginning,
									c = a.isEnd;
								if ((s = s >= a.minTranslate() ? a.minTranslate() : s) <= a.maxTranslate() && (s = a.maxTranslate()), a.setTransition(0), a.setTranslate(s), a.updateProgress(), a.updateActiveIndex(), a.updateSlidesClasses(), (!i && a.isBeginning || !c && a.isEnd) && a.updateSlidesClasses(), a.params.freeModeSticky && (clearTimeout(a.mousewheel.timeout), a.mousewheel.timeout = void 0, 15 <= (l = a.mousewheel.recentWheelEvents).length && l.shift(), i = l.length ? l[l.length - 1] : void 0, c = l[0], l.push(o), i && (o.delta > i.delta || o.direction !== i.direction) ? l.splice(0) : 15 <= l.length && o.time - c.time < 500 && 1 <= c.delta - o.delta && o.delta <= 6 && (d = 0 < n ? .8 : .2, a.mousewheel.lastEventBeforeSnap = o, l.splice(0), a.mousewheel.timeout = K.nextTick(function () {
										a.slideToClosest(a.params.speed, !0, void 0, d)
									}, 0)), a.mousewheel.timeout || (a.mousewheel.timeout = K.nextTick(function () {
										a.mousewheel.lastEventBeforeSnap = o, l.splice(0), a.slideToClosest(a.params.speed, !0, void 0, .5)
									}, 500))), r || a.emit("scroll", t), a.params.autoplay && a.params.autoplayDisableOnInteraction && a.autoplay.stop(), s === a.minTranslate() || s === a.maxTranslate()) return !0
							}
						} else {
							i = {
								time: K.now(),
								delta: Math.abs(n),
								direction: Math.sign(n),
								raw: e
							}, c = a.mousewheel.recentWheelEvents, r = (2 <= c.length && c.shift(), c.length ? c[c.length - 1] : void 0);
							if (c.push(i), (!r || i.direction !== r.direction || i.delta > r.delta) && a.mousewheel.animateSlider(i), a.mousewheel.releaseScroll(i)) return !0
						}
						return t.preventDefault ? t.preventDefault() : t.returnValue = !1, !1
					},
					animateSlider: function (e) {
						var t = this;
						return 6 <= e.delta && K.now() - t.mousewheel.lastScrollTime < 60 || (e.direction < 0 ? t.isEnd && !t.params.loop || t.animating || (t.slideNext(), t.emit("scroll", e.raw)) : t.isBeginning && !t.params.loop || t.animating || (t.slidePrev(), t.emit("scroll", e.raw)), t.mousewheel.lastScrollTime = (new U.window.Date).getTime(), !1)
					},
					releaseScroll: function (e) {
						var t = this,
							a = t.params.mousewheel;
						if (e.direction < 0) {
							if (t.isEnd && !t.params.loop && a.releaseOnEdges) return !0
						} else if (t.isBeginning && !t.params.loop && a.releaseOnEdges) return !0;
						return !1
					},
					enable: function () {
						var e = this,
							t = C.event();
						if (e.params.cssMode) e.wrapperEl.removeEventListener(t, e.mousewheel.handle);
						else {
							if (!t) return !1;
							if (e.mousewheel.enabled) return !1;
							var a = e.$el;
							(a = "container" !== e.params.mousewheel.eventsTarged ? (0, E.$)(e.params.mousewheel.eventsTarged) : a).on("mouseenter", e.mousewheel.handleMouseEnter), a.on("mouseleave", e.mousewheel.handleMouseLeave), a.on(t, e.mousewheel.handle), e.mousewheel.enabled = !0
						}
						return !0
					},
					disable: function () {
						var e = this,
							t = C.event();
						if (e.params.cssMode) e.wrapperEl.addEventListener(t, e.mousewheel.handle);
						else {
							if (!t) return !1;
							if (!e.mousewheel.enabled) return !1;
							var a = e.$el;
							(a = "container" !== e.params.mousewheel.eventsTarged ? (0, E.$)(e.params.mousewheel.eventsTarged) : a).off(t, e.mousewheel.handle), e.mousewheel.enabled = !1
						}
						return !0
					}
				},
				M = {
					update: function () {
						var e, t, a = this,
							i = a.params.navigation;
						a.params.loop || (e = (t = a.navigation).$nextEl, (t = t.$prevEl) && 0 < t.length && (a.isBeginning ? t.addClass(i.disabledClass) : t.removeClass(i.disabledClass), t[a.params.watchOverflow && a.isLocked ? "addClass" : "removeClass"](i.lockClass)), e && 0 < e.length && (a.isEnd ? e.addClass(i.disabledClass) : e.removeClass(i.disabledClass), e[a.params.watchOverflow && a.isLocked ? "addClass" : "removeClass"](i.lockClass)))
					},
					onPrevClick: function (e) {
						e.preventDefault(), this.isBeginning && !this.params.loop || this.slidePrev()
					},
					onNextClick: function (e) {
						e.preventDefault(), this.isEnd && !this.params.loop || this.slideNext()
					},
					init: function () {
						var e, t, a = this,
							i = a.params.navigation;
						(i.nextEl || i.prevEl) && (i.nextEl && (e = (0, E.$)(i.nextEl), a.params.uniqueNavElements && "string" == typeof i.nextEl && 1 < e.length && 1 === a.$el.find(i.nextEl).length && (e = a.$el.find(i.nextEl))), i.prevEl && (t = (0, E.$)(i.prevEl), a.params.uniqueNavElements && "string" == typeof i.prevEl && 1 < t.length && 1 === a.$el.find(i.prevEl).length && (t = a.$el.find(i.prevEl))), e && 0 < e.length && e.on("click", a.navigation.onNextClick), t && 0 < t.length && t.on("click", a.navigation.onPrevClick), K.extend(a.navigation, {
							$nextEl: e,
							nextEl: e && e[0],
							$prevEl: t,
							prevEl: t && t[0]
						}))
					},
					destroy: function () {
						var e = this,
							t = e.navigation,
							a = t.$nextEl,
							t = t.$prevEl;
						a && a.length && (a.off("click", e.navigation.onNextClick), a.removeClass(e.params.navigation.disabledClass)), t && t.length && (t.off("click", e.navigation.onPrevClick), t.removeClass(e.params.navigation.disabledClass))
					}
				},
				P = {
					update: function () {
						var e = this,
							t = e.rtl,
							i = e.params.pagination;
						if (i.el && e.pagination.el && e.pagination.$el && 0 !== e.pagination.$el.length) {
							var n, a = (e.virtual && e.params.virtual.enabled ? e.virtual : e).slides.length,
								s = e.pagination.$el,
								r = e.params.loop ? Math.ceil((a - 2 * e.loopedSlides) / e.params.slidesPerGroup) : e.snapGrid.length;
							if (e.params.loop ? ((n = Math.ceil((e.activeIndex - e.loopedSlides) / e.params.slidesPerGroup)) > a - 1 - 2 * e.loopedSlides && (n -= a - 2 * e.loopedSlides), r - 1 < n && (n -= r), n < 0 && "bullets" !== e.params.paginationType && (n = r + n)) : n = void 0 !== e.snapIndex ? e.snapIndex : e.activeIndex || 0, "bullets" === i.type && e.pagination.bullets && 0 < e.pagination.bullets.length) {
								var o, l, d, c = e.pagination.bullets;
								if (i.dynamicBullets && (e.pagination.bulletSize = c.eq(0)[e.isHorizontal() ? "outerWidth" : "outerHeight"](!0), s.css(e.isHorizontal() ? "width" : "height", "".concat(e.pagination.bulletSize * (i.dynamicMainBullets + 4), "px")), 1 < i.dynamicMainBullets && void 0 !== e.previousIndex && (e.pagination.dynamicBulletIndex += n - e.previousIndex, e.pagination.dynamicBulletIndex > i.dynamicMainBullets - 1 ? e.pagination.dynamicBulletIndex = i.dynamicMainBullets - 1 : e.pagination.dynamicBulletIndex < 0 && (e.pagination.dynamicBulletIndex = 0)), o = n - e.pagination.dynamicBulletIndex, d = ((l = o + (Math.min(c.length, i.dynamicMainBullets) - 1)) + o) / 2), c.removeClass("".concat(i.bulletActiveClass, " ").concat(i.bulletActiveClass, "-next ").concat(i.bulletActiveClass, "-next-next ").concat(i.bulletActiveClass, "-prev ").concat(i.bulletActiveClass, "-prev-prev ").concat(i.bulletActiveClass, "-main")), 1 < s.length) c.each(function (e, t) {
									var t = (0, E.$)(t),
										a = t.index();
									a === n && t.addClass(i.bulletActiveClass), i.dynamicBullets && (o <= a && a <= l && t.addClass("".concat(i.bulletActiveClass, "-main")), a === o && t.prev().addClass("".concat(i.bulletActiveClass, "-prev")).prev().addClass("".concat(i.bulletActiveClass, "-prev-prev")), a === l && t.next().addClass("".concat(i.bulletActiveClass, "-next")).next().addClass("".concat(i.bulletActiveClass, "-next-next")))
								});
								else {
									var a = c.eq(n),
										u = a.index();
									if (a.addClass(i.bulletActiveClass), i.dynamicBullets) {
										for (var a = c.eq(o), p = c.eq(l), h = o; h <= l; h += 1) c.eq(h).addClass("".concat(i.bulletActiveClass, "-main"));
										if (e.params.loop)
											if (u >= c.length - i.dynamicMainBullets) {
												for (var f = i.dynamicMainBullets; 0 <= f; --f) c.eq(c.length - f).addClass("".concat(i.bulletActiveClass, "-main"));
												c.eq(c.length - i.dynamicMainBullets - 1).addClass("".concat(i.bulletActiveClass, "-prev"))
											} else a.prev().addClass("".concat(i.bulletActiveClass, "-prev")).prev().addClass("".concat(i.bulletActiveClass, "-prev-prev")), p.next().addClass("".concat(i.bulletActiveClass, "-next")).next().addClass("".concat(i.bulletActiveClass, "-next-next"));
										else a.prev().addClass("".concat(i.bulletActiveClass, "-prev")).prev().addClass("".concat(i.bulletActiveClass, "-prev-prev")), p.next().addClass("".concat(i.bulletActiveClass, "-next")).next().addClass("".concat(i.bulletActiveClass, "-next-next"))
									}
								}
								i.dynamicBullets && (u = Math.min(c.length, i.dynamicMainBullets + 4), a = (e.pagination.bulletSize * u - e.pagination.bulletSize) / 2 - d * e.pagination.bulletSize, p = t ? "right" : "left", c.css(e.isHorizontal() ? p : "top", "".concat(a, "px")))
							}
							"fraction" === i.type && (s.find(".".concat(i.currentClass)).text(i.formatFractionCurrent(n + 1)), s.find(".".concat(i.totalClass)).text(i.formatFractionTotal(r))), "progressbar" === i.type && (u = i.progressbarOpposite ? e.isHorizontal() ? "vertical" : "horizontal" : e.isHorizontal() ? "horizontal" : "vertical", d = (n + 1) / r, p = t = 1, "horizontal" === u ? t = d : p = d, s.find(".".concat(i.progressbarFillClass)).transform("translate3d(0,0,0) scaleX(".concat(t, ") scaleY(").concat(p, ")")).transition(e.params.speed)), "custom" === i.type && i.renderCustom ? (s.html(i.renderCustom(e, n + 1, r)), e.emit("paginationRender", e, s[0])) : e.emit("paginationUpdate", e, s[0]), s[e.params.watchOverflow && e.isLocked ? "addClass" : "removeClass"](i.lockClass)
						}
					},
					render: function () {
						var e = this,
							t = e.params.pagination;
						if (t.el && e.pagination.el && e.pagination.$el && 0 !== e.pagination.$el.length) {
							var a = (e.virtual && e.params.virtual.enabled ? e.virtual : e).slides.length,
								i = e.pagination.$el,
								n = "";
							if ("bullets" === t.type) {
								for (var s = e.params.loop ? Math.ceil((a - 2 * e.loopedSlides) / e.params.slidesPerGroup) : e.snapGrid.length, r = 0; r < s; r += 1) t.renderBullet ? n += t.renderBullet.call(e, r, t.bulletClass) : n += "<".concat(t.bulletElement, ' class="').concat(t.bulletClass, '"></').concat(t.bulletElement, ">");
								i.html(n), e.pagination.bullets = i.find(".".concat(t.bulletClass))
							}
							"fraction" === t.type && (n = t.renderFraction ? t.renderFraction.call(e, t.currentClass, t.totalClass) : '<span class="'.concat(t.currentClass, '"></span>') + " / " + '<span class="'.concat(t.totalClass, '"></span>'), i.html(n)), "progressbar" === t.type && (n = t.renderProgressbar ? t.renderProgressbar.call(e, t.progressbarFillClass) : '<span class="'.concat(t.progressbarFillClass, '"></span>'), i.html(n)), "custom" !== t.type && e.emit("paginationRender", e.pagination.$el[0])
						}
					},
					init: function () {
						var e, t = this,
							a = t.params.pagination;
						!a.el || 0 !== (e = (0, E.$)(a.el)).length && (t.params.uniqueNavElements && "string" == typeof a.el && 1 < e.length && 1 === t.$el.find(a.el).length && (e = t.$el.find(a.el)), "bullets" === a.type && a.clickable && e.addClass(a.clickableClass), e.addClass(a.modifierClass + a.type), "bullets" === a.type && a.dynamicBullets && (e.addClass("".concat(a.modifierClass).concat(a.type, "-dynamic")), t.pagination.dynamicBulletIndex = 0, a.dynamicMainBullets < 1 && (a.dynamicMainBullets = 1)), "progressbar" === a.type && a.progressbarOpposite && e.addClass(a.progressbarOppositeClass), a.clickable && e.on("click", ".".concat(a.bulletClass), function (e) {
							e.preventDefault();
							e = (0, E.$)(this).index() * t.params.slidesPerGroup;
							t.params.loop && (e += t.loopedSlides), t.slideTo(e)
						}), K.extend(t.pagination, {
							$el: e,
							el: e[0]
						}))
					},
					destroy: function () {
						var e, t = this,
							a = t.params.pagination;
						a.el && t.pagination.el && t.pagination.$el && 0 !== t.pagination.$el.length && ((e = t.pagination.$el).removeClass(a.hiddenClass), e.removeClass(a.modifierClass + a.type), t.pagination.bullets && t.pagination.bullets.removeClass(a.bulletActiveClass), a.clickable && e.off("click", ".".concat(a.bulletClass)))
					}
				},
				k = {
					setTranslate: function () {
						var e, t, a, i, n, s, r, o, l = this;
						l.params.scrollbar.el && l.scrollbar.el && (s = l.scrollbar, e = l.rtlTranslate, o = l.progress, t = s.dragSize, a = s.trackSize, i = s.$dragEl, n = s.$el, s = l.params.scrollbar, o = (a - (r = t)) * o, e ? 0 < (o = -o) ? (r = t - o, o = 0) : a < -o + t && (r = a + o) : o < 0 ? (r = t + o, o = 0) : a < o + t && (r = a - o), l.isHorizontal() ? (i.transform("translate3d(".concat(o, "px, 0, 0)")), i[0].style.width = "".concat(r, "px")) : (i.transform("translate3d(0px, ".concat(o, "px, 0)")), i[0].style.height = "".concat(r, "px")), s.hide && (clearTimeout(l.scrollbar.timeout), n[0].style.opacity = 1, l.scrollbar.timeout = setTimeout(function () {
							n[0].style.opacity = 0, n.transition(400)
						}, 1e3)))
					},
					setTransition: function (e) {
						this.params.scrollbar.el && this.scrollbar.el && this.scrollbar.$dragEl.transition(e)
					},
					updateSize: function () {
						var e, t, a, i, n, s, r, o = this;
						o.params.scrollbar.el && o.scrollbar.el && (t = (e = o.scrollbar).$dragEl, a = e.$el, t[0].style.width = "", t[0].style.height = "", i = o.isHorizontal() ? a[0].offsetWidth : a[0].offsetHeight, s = (n = o.size / o.virtualSize) * (i / o.size), r = "auto" === o.params.scrollbar.dragSize ? i * n : parseInt(o.params.scrollbar.dragSize, 10), o.isHorizontal() ? t[0].style.width = "".concat(r, "px") : t[0].style.height = "".concat(r, "px"), a[0].style.display = 1 <= n ? "none" : "", o.params.scrollbar.hide && (a[0].style.opacity = 0), K.extend(e, {
							trackSize: i,
							divider: n,
							moveDivider: s,
							dragSize: r
						}), e.$el[o.params.watchOverflow && o.isLocked ? "addClass" : "removeClass"](o.params.scrollbar.lockClass))
					},
					getPointerPosition: function (e) {
						return this.isHorizontal() ? ("touchstart" === e.type || "touchmove" === e.type ? e.targetTouches[0] : e).clientX : ("touchstart" === e.type || "touchmove" === e.type ? e.targetTouches[0] : e).clientY
					},
					setDragPosition: function (e) {
						var t = this,
							a = t.scrollbar,
							i = t.rtlTranslate,
							n = a.$el,
							s = a.dragSize,
							r = a.trackSize,
							o = a.dragStartPos,
							a = (a.getPointerPosition(e) - n.offset()[t.isHorizontal() ? "left" : "top"] - (null !== o ? o : s / 2)) / (r - s),
							e = (a = Math.max(Math.min(a, 1), 0), i && (a = 1 - a), t.minTranslate() + (t.maxTranslate() - t.minTranslate()) * a);
						t.updateProgress(e), t.setTranslate(e), t.updateActiveIndex(), t.updateSlidesClasses()
					},
					onDragStart: function (e) {
						var t = this,
							a = t.params.scrollbar,
							i = t.scrollbar,
							n = t.$wrapperEl,
							s = i.$el,
							r = i.$dragEl;
						t.scrollbar.isTouched = !0, t.scrollbar.dragStartPos = e.target === r[0] || e.target === r ? i.getPointerPosition(e) - e.target.getBoundingClientRect()[t.isHorizontal() ? "left" : "top"] : null, e.preventDefault(), e.stopPropagation(), n.transition(100), r.transition(100), i.setDragPosition(e), clearTimeout(t.scrollbar.dragTimeout), s.transition(0), a.hide && s.css("opacity", 1), t.params.cssMode && t.$wrapperEl.css("scroll-snap-type", "none"), t.emit("scrollbarDragStart", e)
					},
					onDragMove: function (e) {
						var t = this.scrollbar,
							a = this.$wrapperEl,
							i = t.$el,
							n = t.$dragEl;
						this.scrollbar.isTouched && (e.preventDefault ? e.preventDefault() : e.returnValue = !1, t.setDragPosition(e), a.transition(0), i.transition(0), n.transition(0), this.emit("scrollbarDragMove", e))
					},
					onDragEnd: function (e) {
						var t = this,
							a = t.params.scrollbar,
							i = t.scrollbar,
							n = t.$wrapperEl,
							s = i.$el;
						t.scrollbar.isTouched && (t.scrollbar.isTouched = !1, t.params.cssMode && (t.$wrapperEl.css("scroll-snap-type", ""), n.transition("")), a.hide && (clearTimeout(t.scrollbar.dragTimeout), t.scrollbar.dragTimeout = K.nextTick(function () {
							s.css("opacity", 0), s.transition(400)
						}, 1e3)), t.emit("scrollbarDragEnd", e), a.snapOnRelease && t.slideToClosest())
					},
					enableDraggable: function () {
						var e, t, a, i, n, s = this;
						s.params.scrollbar.el && (a = s.scrollbar, e = s.touchEventsTouch, t = s.touchEventsDesktop, n = s.params, a = a.$el[0], i = !(!b.passiveListener || !n.passiveListeners) && {
							passive: !1,
							capture: !1
						}, n = !(!b.passiveListener || !n.passiveListeners) && {
							passive: !0,
							capture: !1
						}, b.touch ? (a.addEventListener(e.start, s.scrollbar.onDragStart, i), a.addEventListener(e.move, s.scrollbar.onDragMove, i), a.addEventListener(e.end, s.scrollbar.onDragEnd, n)) : (a.addEventListener(t.start, s.scrollbar.onDragStart, i), U.document.addEventListener(t.move, s.scrollbar.onDragMove, i), U.document.addEventListener(t.end, s.scrollbar.onDragEnd, n)))
					},
					disableDraggable: function () {
						var e, t, a, i, n, s = this;
						s.params.scrollbar.el && (a = s.scrollbar, e = s.touchEventsTouch, t = s.touchEventsDesktop, n = s.params, a = a.$el[0], i = !(!b.passiveListener || !n.passiveListeners) && {
							passive: !1,
							capture: !1
						}, n = !(!b.passiveListener || !n.passiveListeners) && {
							passive: !0,
							capture: !1
						}, b.touch ? (a.removeEventListener(e.start, s.scrollbar.onDragStart, i), a.removeEventListener(e.move, s.scrollbar.onDragMove, i), a.removeEventListener(e.end, s.scrollbar.onDragEnd, n)) : (a.removeEventListener(t.start, s.scrollbar.onDragStart, i), U.document.removeEventListener(t.move, s.scrollbar.onDragMove, i), U.document.removeEventListener(t.end, s.scrollbar.onDragEnd, n)))
					},
					init: function () {
						var e, t, a, i, n = this;
						n.params.scrollbar.el && (e = n.scrollbar, i = n.$el, t = n.params.scrollbar, a = (0, E.$)(t.el), 0 === (i = (a = n.params.uniqueNavElements && "string" == typeof t.el && 1 < a.length && 1 === i.find(t.el).length ? i.find(t.el) : a).find(".".concat(n.params.scrollbar.dragClass))).length && (i = (0, E.$)('<div class="'.concat(n.params.scrollbar.dragClass, '"></div>')), a.append(i)), K.extend(e, {
							$el: a,
							el: a[0],
							$dragEl: i,
							dragEl: i[0]
						}), t.draggable && e.enableDraggable())
					},
					destroy: function () {
						this.scrollbar.disableDraggable()
					}
				},
				j = {
					setTransform: function (e, t) {
						var a = this.rtl,
							e = (0, E.$)(e),
							a = a ? -1 : 1,
							i = e.attr("data-swiper-parallax") || "0",
							n = e.attr("data-swiper-parallax-x"),
							s = e.attr("data-swiper-parallax-y"),
							r = e.attr("data-swiper-parallax-scale"),
							o = e.attr("data-swiper-parallax-opacity");
						n || s ? (n = n || "0", s = s || "0") : this.isHorizontal() ? (n = i, s = "0") : (s = i, n = "0"), n = 0 <= n.indexOf("%") ? "".concat(parseInt(n, 10) * t * a, "%") : "".concat(n * t * a, "px"), s = 0 <= s.indexOf("%") ? "".concat(parseInt(s, 10) * t, "%") : "".concat(s * t, "px"), null != o && (i = o - (o - 1) * (1 - Math.abs(t)), e[0].style.opacity = i), null == r ? e.transform("translate3d(".concat(n, ", ").concat(s, ", 0px)")) : (a = r - (r - 1) * (1 - Math.abs(t)), e.transform("translate3d(".concat(n, ", ").concat(s, ", 0px) scale(").concat(a, ")")))
					},
					setTranslate: function () {
						var i = this,
							e = i.$el,
							t = i.slides,
							n = i.progress,
							s = i.snapGrid;
						e.children("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y], [data-swiper-parallax-opacity], [data-swiper-parallax-scale]").each(function (e, t) {
							i.parallax.setTransform(t, n)
						}), t.each(function (e, t) {
							var a = t.progress;
							1 < i.params.slidesPerGroup && "auto" !== i.params.slidesPerView && (a += Math.ceil(e / 2) - n * (s.length - 1)), a = Math.min(Math.max(a, -1), 1), (0, E.$)(t).find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y], [data-swiper-parallax-opacity], [data-swiper-parallax-scale]").each(function (e, t) {
								i.parallax.setTransform(t, a)
							})
						})
					},
					setTransition: function () {
						var i = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : this.params.speed;
						this.$el.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y], [data-swiper-parallax-opacity], [data-swiper-parallax-scale]").each(function (e, t) {
							var t = (0, E.$)(t),
								a = parseInt(t.attr("data-swiper-parallax-duration"), 10) || i;
							t.transition(a = 0 === i ? 0 : a)
						})
					}
				},
				W = {
					getDistanceBetweenTouches: function (e) {
						var t, a, i;
						return e.targetTouches.length < 2 ? 1 : (t = e.targetTouches[0].pageX, a = e.targetTouches[0].pageY, i = e.targetTouches[1].pageX, e = e.targetTouches[1].pageY, Math.sqrt(Math.pow(i - t, 2) + Math.pow(e - a, 2)))
					},
					onGestureStart: function (e) {
						var t = this,
							a = t.params.zoom,
							i = t.zoom,
							n = i.gesture;
						if (i.fakeGestureTouched = !1, i.fakeGestureMoved = !1, !b.gestures) {
							if ("touchstart" !== e.type || "touchstart" === e.type && e.targetTouches.length < 2) return;
							i.fakeGestureTouched = !0, n.scaleStart = W.getDistanceBetweenTouches(e)
						}
						n.$slideEl && n.$slideEl.length || (n.$slideEl = (0, E.$)(e.target).closest(".".concat(t.params.slideClass)), 0 === n.$slideEl.length && (n.$slideEl = t.slides.eq(t.activeIndex)), n.$imageEl = n.$slideEl.find("img, svg, canvas, picture, .swiper-zoom-target"), n.$imageWrapEl = n.$imageEl.parent(".".concat(a.containerClass)), n.maxRatio = n.$imageWrapEl.attr("data-swiper-zoom") || a.maxRatio, 0 !== n.$imageWrapEl.length) ? (n.$imageEl && n.$imageEl.transition(0), t.zoom.isScaling = !0) : n.$imageEl = void 0
					},
					onGestureChange: function (e) {
						var t = this.params.zoom,
							a = this.zoom,
							i = a.gesture;
						if (!b.gestures) {
							if ("touchmove" !== e.type || "touchmove" === e.type && e.targetTouches.length < 2) return;
							a.fakeGestureMoved = !0, i.scaleMove = W.getDistanceBetweenTouches(e)
						}
						i.$imageEl && 0 !== i.$imageEl.length && (a.scale = b.gestures ? e.scale * a.currentScale : i.scaleMove / i.scaleStart * a.currentScale, a.scale > i.maxRatio && (a.scale = i.maxRatio - 1 + Math.pow(a.scale - i.maxRatio + 1, .5)), a.scale < t.minRatio && (a.scale = t.minRatio + 1 - Math.pow(t.minRatio - a.scale + 1, .5)), i.$imageEl.transform("translate3d(0,0,0) scale(".concat(a.scale, ")")))
					},
					onGestureEnd: function (e) {
						var t = this.params.zoom,
							a = this.zoom,
							i = a.gesture;
						if (!b.gestures) {
							if (!a.fakeGestureTouched || !a.fakeGestureMoved) return;
							if ("touchend" !== e.type || "touchend" === e.type && e.changedTouches.length < 2 && !o.android) return;
							a.fakeGestureTouched = !1, a.fakeGestureMoved = !1
						}
						i.$imageEl && 0 !== i.$imageEl.length && (a.scale = Math.max(Math.min(a.scale, i.maxRatio), t.minRatio), i.$imageEl.transition(this.params.speed).transform("translate3d(0,0,0) scale(".concat(a.scale, ")")), a.currentScale = a.scale, a.isScaling = !1, 1 === a.scale && (i.$slideEl = void 0))
					},
					onTouchStart: function (e) {
						var t = this.zoom,
							a = t.gesture,
							t = t.image;
						a.$imageEl && 0 !== a.$imageEl.length && !t.isTouched && (o.android && e.preventDefault(), t.isTouched = !0, t.touchesStart.x = ("touchstart" === e.type ? e.targetTouches[0] : e).pageX, t.touchesStart.y = ("touchstart" === e.type ? e.targetTouches[0] : e).pageY)
					},
					onTouchMove: function (e) {
						var t = this,
							a = t.zoom,
							i = a.gesture,
							n = a.image,
							s = a.velocity;
						if (i.$imageEl && 0 !== i.$imageEl.length && (t.allowClick = !1, n.isTouched && i.$slideEl)) {
							n.isMoved || (n.width = i.$imageEl[0].offsetWidth, n.height = i.$imageEl[0].offsetHeight, n.startX = K.getTranslate(i.$imageWrapEl[0], "x") || 0, n.startY = K.getTranslate(i.$imageWrapEl[0], "y") || 0, i.slideWidth = i.$slideEl[0].offsetWidth, i.slideHeight = i.$slideEl[0].offsetHeight, i.$imageWrapEl.transition(0), t.rtl && (n.startX = -n.startX, n.startY = -n.startY));
							var r = n.width * a.scale,
								o = n.height * a.scale;
							if (!(r < i.slideWidth && o < i.slideHeight)) {
								if (n.minX = Math.min(i.slideWidth / 2 - r / 2, 0), n.maxX = -n.minX, n.minY = Math.min(i.slideHeight / 2 - o / 2, 0), n.maxY = -n.minY, n.touchesCurrent.x = ("touchmove" === e.type ? e.targetTouches[0] : e).pageX, n.touchesCurrent.y = ("touchmove" === e.type ? e.targetTouches[0] : e).pageY, !n.isMoved && !a.isScaling) {
									if (t.isHorizontal() && (Math.floor(n.minX) === Math.floor(n.startX) && n.touchesCurrent.x < n.touchesStart.x || Math.floor(n.maxX) === Math.floor(n.startX) && n.touchesCurrent.x > n.touchesStart.x)) return void(n.isTouched = !1);
									if (!t.isHorizontal() && (Math.floor(n.minY) === Math.floor(n.startY) && n.touchesCurrent.y < n.touchesStart.y || Math.floor(n.maxY) === Math.floor(n.startY) && n.touchesCurrent.y > n.touchesStart.y)) return void(n.isTouched = !1)
								}
								e.preventDefault(), e.stopPropagation(), n.isMoved = !0, n.currentX = n.touchesCurrent.x - n.touchesStart.x + n.startX, n.currentY = n.touchesCurrent.y - n.touchesStart.y + n.startY, n.currentX < n.minX && (n.currentX = n.minX + 1 - Math.pow(n.minX - n.currentX + 1, .8)), n.currentX > n.maxX && (n.currentX = n.maxX - 1 + Math.pow(n.currentX - n.maxX + 1, .8)), n.currentY < n.minY && (n.currentY = n.minY + 1 - Math.pow(n.minY - n.currentY + 1, .8)), n.currentY > n.maxY && (n.currentY = n.maxY - 1 + Math.pow(n.currentY - n.maxY + 1, .8)), s.prevPositionX || (s.prevPositionX = n.touchesCurrent.x), s.prevPositionY || (s.prevPositionY = n.touchesCurrent.y), s.prevTime || (s.prevTime = Date.now()), s.x = (n.touchesCurrent.x - s.prevPositionX) / (Date.now() - s.prevTime) / 2, s.y = (n.touchesCurrent.y - s.prevPositionY) / (Date.now() - s.prevTime) / 2, Math.abs(n.touchesCurrent.x - s.prevPositionX) < 2 && (s.x = 0), Math.abs(n.touchesCurrent.y - s.prevPositionY) < 2 && (s.y = 0), s.prevPositionX = n.touchesCurrent.x, s.prevPositionY = n.touchesCurrent.y, s.prevTime = Date.now(), i.$imageWrapEl.transform("translate3d(".concat(n.currentX, "px, ").concat(n.currentY, "px,0)"))
							}
						}
					},
					onTouchEnd: function () {
						var e, t, a, i, n = this.zoom,
							s = n.gesture,
							r = n.image,
							o = n.velocity;
						s.$imageEl && 0 !== s.$imageEl.length && (r.isTouched && r.isMoved ? (r.isTouched = !1, r.isMoved = !1, e = o.x * (a = 300), e = r.currentX + e, t = o.y * (i = 300), t = r.currentY + t, 0 !== o.x && (a = Math.abs((e - r.currentX) / o.x)), 0 !== o.y && (i = Math.abs((t - r.currentY) / o.y)), o = Math.max(a, i), r.currentX = e, r.currentY = t, a = r.width * n.scale, i = r.height * n.scale, r.minX = Math.min(s.slideWidth / 2 - a / 2, 0), r.maxX = -r.minX, r.minY = Math.min(s.slideHeight / 2 - i / 2, 0), r.maxY = -r.minY, r.currentX = Math.max(Math.min(r.currentX, r.maxX), r.minX), r.currentY = Math.max(Math.min(r.currentY, r.maxY), r.minY), s.$imageWrapEl.transition(o).transform("translate3d(".concat(r.currentX, "px, ").concat(r.currentY, "px,0)"))) : (r.isTouched = !1, r.isMoved = !1))
					},
					onTransitionEnd: function () {
						var e = this.zoom,
							t = e.gesture;
						t.$slideEl && this.previousIndex !== this.activeIndex && (t.$imageEl && t.$imageEl.transform("translate3d(0,0,0) scale(1)"), t.$imageWrapEl && t.$imageWrapEl.transform("translate3d(0,0,0)"), e.scale = 1, e.currentScale = 1, t.$slideEl = void 0, t.$imageEl = void 0, t.$imageWrapEl = void 0)
					},
					toggle: function (e) {
						var t = this.zoom;
						t.scale && 1 !== t.scale ? t.out() : t.in(e)
					},
					in: function (e) {
						var t, a, i, n = this,
							s = n.zoom,
							r = n.params.zoom,
							o = s.gesture,
							l = s.image;
						o.$slideEl || (n.params.virtual && n.params.virtual.enabled && n.virtual ? o.$slideEl = n.$wrapperEl.children(".".concat(n.params.slideActiveClass)) : o.$slideEl = n.slides.eq(n.activeIndex), o.$imageEl = o.$slideEl.find("img, svg, canvas, picture, .swiper-zoom-target"), o.$imageWrapEl = o.$imageEl.parent(".".concat(r.containerClass))), o.$imageEl && 0 !== o.$imageEl.length && (o.$slideEl.addClass("".concat(r.zoomedSlideClass)), n = void 0 === l.touchesStart.x && e ? (t = ("touchend" === e.type ? e.changedTouches[0] : e).pageX, ("touchend" === e.type ? e.changedTouches[0] : e).pageY) : (t = l.touchesStart.x, l.touchesStart.y), s.scale = o.$imageWrapEl.attr("data-swiper-zoom") || r.maxRatio, s.currentScale = o.$imageWrapEl.attr("data-swiper-zoom") || r.maxRatio, e ? (l = o.$slideEl[0].offsetWidth, r = o.$slideEl[0].offsetHeight, e = o.$slideEl.offset().left + l / 2 - t, t = o.$slideEl.offset().top + r / 2 - n, n = o.$imageEl[0].offsetWidth, i = o.$imageEl[0].offsetHeight, n = n * s.scale, i = i * s.scale, l = Math.min(l / 2 - n / 2, 0), n = Math.min(r / 2 - i / 2, 0), (r = -l) < (i = (i = e * s.scale) < l ? l : i) && (i = r), (e = -n) < (a = (a = t * s.scale) < n ? n : a) && (a = e)) : a = i = 0, o.$imageWrapEl.transition(300).transform("translate3d(".concat(i, "px, ").concat(a, "px,0)")), o.$imageEl.transition(300).transform("translate3d(0,0,0) scale(".concat(s.scale, ")")))
					},
					out: function () {
						var e = this,
							t = e.zoom,
							a = e.params.zoom,
							i = t.gesture;
						i.$slideEl || (e.params.virtual && e.params.virtual.enabled && e.virtual ? i.$slideEl = e.$wrapperEl.children(".".concat(e.params.slideActiveClass)) : i.$slideEl = e.slides.eq(e.activeIndex), i.$imageEl = i.$slideEl.find("img, svg, canvas, picture, .swiper-zoom-target"), i.$imageWrapEl = i.$imageEl.parent(".".concat(a.containerClass))), i.$imageEl && 0 !== i.$imageEl.length && (t.scale = 1, t.currentScale = 1, i.$imageWrapEl.transition(300).transform("translate3d(0,0,0)"), i.$imageEl.transition(300).transform("translate3d(0,0,0) scale(1)"), i.$slideEl.removeClass("".concat(a.zoomedSlideClass)), i.$slideEl = void 0)
					},
					enable: function () {
						var e, t, a, i = this,
							n = i.zoom;
						n.enabled || (n.enabled = !0, e = !("touchstart" !== i.touchEvents.start || !b.passiveListener || !i.params.passiveListeners) && {
							passive: !0,
							capture: !1
						}, t = !b.passiveListener || {
							passive: !1,
							capture: !0
						}, a = ".".concat(i.params.slideClass), b.gestures ? (i.$wrapperEl.on("gesturestart", a, n.onGestureStart, e), i.$wrapperEl.on("gesturechange", a, n.onGestureChange, e), i.$wrapperEl.on("gestureend", a, n.onGestureEnd, e)) : "touchstart" === i.touchEvents.start && (i.$wrapperEl.on(i.touchEvents.start, a, n.onGestureStart, e), i.$wrapperEl.on(i.touchEvents.move, a, n.onGestureChange, t), i.$wrapperEl.on(i.touchEvents.end, a, n.onGestureEnd, e), i.touchEvents.cancel && i.$wrapperEl.on(i.touchEvents.cancel, a, n.onGestureEnd, e)), i.$wrapperEl.on(i.touchEvents.move, ".".concat(i.params.zoom.containerClass), n.onTouchMove, t))
					},
					disable: function () {
						var e, t, a, i = this,
							n = i.zoom;
						n.enabled && (i.zoom.enabled = !1, e = !("touchstart" !== i.touchEvents.start || !b.passiveListener || !i.params.passiveListeners) && {
							passive: !0,
							capture: !1
						}, t = !b.passiveListener || {
							passive: !1,
							capture: !0
						}, a = ".".concat(i.params.slideClass), b.gestures ? (i.$wrapperEl.off("gesturestart", a, n.onGestureStart, e), i.$wrapperEl.off("gesturechange", a, n.onGestureChange, e), i.$wrapperEl.off("gestureend", a, n.onGestureEnd, e)) : "touchstart" === i.touchEvents.start && (i.$wrapperEl.off(i.touchEvents.start, a, n.onGestureStart, e), i.$wrapperEl.off(i.touchEvents.move, a, n.onGestureChange, t), i.$wrapperEl.off(i.touchEvents.end, a, n.onGestureEnd, e), i.touchEvents.cancel && i.$wrapperEl.off(i.touchEvents.cancel, a, n.onGestureEnd, e)), i.$wrapperEl.off(i.touchEvents.move, ".".concat(i.params.zoom.containerClass), n.onTouchMove, t))
					}
				},
				_ = {
					loadInSlide: function (e) {
						var o, l = !(1 < arguments.length && void 0 !== arguments[1]) || arguments[1],
							d = this,
							c = d.params.lazy;
						void 0 !== e && 0 !== d.slides.length && (e = (o = d.virtual && d.params.virtual.enabled ? d.$wrapperEl.children(".".concat(d.params.slideClass, '[data-swiper-slide-index="').concat(e, '"]')) : d.slides.eq(e)).find(".".concat(c.elementClass, ":not(.").concat(c.loadedClass, "):not(.").concat(c.loadingClass, ")")), 0 !== (e = !o.hasClass(c.elementClass) || o.hasClass(c.loadedClass) || o.hasClass(c.loadingClass) ? e : e.add(o[0])).length && e.each(function (e, t) {
							var a = (0, E.$)(t),
								i = (a.addClass(c.loadingClass), a.attr("data-background")),
								n = a.attr("data-src"),
								s = a.attr("data-srcset"),
								r = a.attr("data-sizes");
							d.loadImage(a[0], n || i, s, r, !1, function () {
								var e, t;
								null == d || !d || d && !d.params || d.destroyed || (i ? (a.css("background-image", 'url("'.concat(i, '")')), a.removeAttr("data-background")) : (s && (a.attr("srcset", s), a.removeAttr("data-srcset")), r && (a.attr("sizes", r), a.removeAttr("data-sizes")), n && (a.attr("src", n), a.removeAttr("data-src"))), a.addClass(c.loadedClass).removeClass(c.loadingClass), o.find(".".concat(c.preloaderClass)).remove(), d.params.loop && l && (e = o.attr("data-swiper-slide-index"), o.hasClass(d.params.slideDuplicateClass) ? (t = d.$wrapperEl.children('[data-swiper-slide-index="'.concat(e, '"]:not(.').concat(d.params.slideDuplicateClass, ")")), d.lazy.loadInSlide(t.index(), !1)) : (t = d.$wrapperEl.children(".".concat(d.params.slideDuplicateClass, '[data-swiper-slide-index="').concat(e, '"]')), d.lazy.loadInSlide(t.index(), !1))), d.emit("lazyImageReady", o[0], a[0]), d.params.autoHeight && d.updateAutoHeight())
							}), d.emit("lazyImageLoad", o[0], a[0])
						}))
					},
					load: function () {
						var a = this,
							t = a.$wrapperEl,
							i = a.params,
							n = a.slides,
							e = a.activeIndex,
							s = a.virtual && i.virtual.enabled,
							r = i.lazy,
							o = i.slidesPerView;

						function l(e) {
							if (s) {
								if (t.children(".".concat(i.slideClass, '[data-swiper-slide-index="').concat(e, '"]')).length) return 1
							} else if (n[e]) return 1
						}

						function d(e) {
							return s ? (0, E.$)(e).attr("data-swiper-slide-index") : (0, E.$)(e).index()
						}
						if ("auto" === o && (o = 0), a.lazy.initialImageLoaded || (a.lazy.initialImageLoaded = !0), a.params.watchSlidesVisibility) t.children(".".concat(i.slideVisibleClass)).each(function (e, t) {
							t = s ? (0, E.$)(t).attr("data-swiper-slide-index") : (0, E.$)(t).index();
							a.lazy.loadInSlide(t)
						});
						else if (1 < o)
							for (var c = e; c < e + o; c += 1) l(c) && a.lazy.loadInSlide(c);
						else a.lazy.loadInSlide(e);
						if (r.loadPrevNext)
							if (1 < o || r.loadPrevNextAmount && 1 < r.loadPrevNextAmount) {
								for (var r = r.loadPrevNextAmount, u = o, p = Math.min(e + u + Math.max(r, u), n.length), u = Math.max(e - Math.max(u, r), 0), h = e + o; h < p; h += 1) l(h) && a.lazy.loadInSlide(h);
								for (var f = u; f < e; f += 1) l(f) && a.lazy.loadInSlide(f)
							} else {
								r = t.children(".".concat(i.slideNextClass)), u = (0 < r.length && a.lazy.loadInSlide(d(r)), t.children(".".concat(i.slidePrevClass)));
								0 < u.length && a.lazy.loadInSlide(d(u))
							}
					}
				},
				$ = {
					LinearSpline: function (e, t) {
						var a, i, n, s, r, o = function (e, t) {
							for (i = -1, a = e.length; 1 < a - i;) e[n = a + i >> 1] <= t ? i = n : a = n;
							return a
						};
						return this.x = e, this.y = t, this.lastIndex = e.length - 1, this.interpolate = function (e) {
							return e ? (r = o(this.x, e), s = r - 1, (e - this.x[s]) * (this.y[r] - this.y[s]) / (this.x[r] - this.x[s]) + this.y[s]) : 0
						}, this
					},
					getInterpolateFunction: function (e) {
						var t = this;
						t.controller.spline || (t.controller.spline = t.params.loop ? new $.LinearSpline(t.slidesGrid, e.slidesGrid) : new $.LinearSpline(t.snapGrid, e.snapGrid))
					},
					setTranslate: function (e, t) {
						var a, i, n = this,
							s = n.controller.control;

						function r(e) {
							var t = n.rtlTranslate ? -n.translate : n.translate;
							"slide" === n.params.controller.by && (n.controller.getInterpolateFunction(e), i = -n.controller.spline.interpolate(-t)), i && "container" !== n.params.controller.by || (a = (e.maxTranslate() - e.minTranslate()) / (n.maxTranslate() - n.minTranslate()), i = (t - n.minTranslate()) * a + e.minTranslate()), n.params.controller.inverse && (i = e.maxTranslate() - i), e.updateProgress(i), e.setTranslate(i, n), e.updateActiveIndex(), e.updateSlidesClasses()
						}
						if (Array.isArray(s))
							for (var o = 0; o < s.length; o += 1) s[o] !== t && s[o] instanceof c && r(s[o]);
						else s instanceof c && t !== s && r(s)
					},
					setTransition: function (t, e) {
						var a, i = this,
							n = i.controller.control;

						function s(e) {
							e.setTransition(t, i), 0 !== t && (e.transitionStart(), e.params.autoHeight && K.nextTick(function () {
								e.updateAutoHeight()
							}), e.$wrapperEl.transitionEnd(function () {
								n && (e.params.loop && "slide" === i.params.controller.by && e.loopFix(), e.transitionEnd())
							}))
						}
						if (Array.isArray(n))
							for (a = 0; a < n.length; a += 1) n[a] !== e && n[a] instanceof c && s(n[a]);
						else n instanceof c && e !== n && s(n)
					}
				},
				Z = {
					makeElFocusable: function (e) {
						return e.attr("tabIndex", "0"), e
					},
					addElRole: function (e, t) {
						return e.attr("role", t), e
					},
					addElLabel: function (e, t) {
						return e.attr("aria-label", t), e
					},
					disableEl: function (e) {
						return e.attr("aria-disabled", !0), e
					},
					enableEl: function (e) {
						return e.attr("aria-disabled", !1), e
					},
					onEnterKey: function (e) {
						var t = this,
							a = t.params.a11y;
						13 === e.keyCode && (e = (0, E.$)(e.target), t.navigation && t.navigation.$nextEl && e.is(t.navigation.$nextEl) && (t.isEnd && !t.params.loop || t.slideNext(), t.isEnd ? t.a11y.notify(a.lastSlideMessage) : t.a11y.notify(a.nextSlideMessage)), t.navigation && t.navigation.$prevEl && e.is(t.navigation.$prevEl) && (t.isBeginning && !t.params.loop || t.slidePrev(), t.isBeginning ? t.a11y.notify(a.firstSlideMessage) : t.a11y.notify(a.prevSlideMessage)), t.pagination && e.is(".".concat(t.params.pagination.bulletClass)) && e[0].click())
					},
					notify: function (e) {
						var t = this.a11y.liveRegion;
						0 !== t.length && (t.html(""), t.html(e))
					},
					updateNavigation: function () {
						var e, t, a = this;
						!a.params.loop && a.navigation && (e = (t = a.navigation).$nextEl, (t = t.$prevEl) && 0 < t.length && (a.isBeginning ? a.a11y.disableEl(t) : a.a11y.enableEl(t)), e && 0 < e.length && (a.isEnd ? a.a11y.disableEl(e) : a.a11y.enableEl(e)))
					},
					updatePagination: function () {
						var a = this,
							i = a.params.a11y;
						a.pagination && a.params.pagination.clickable && a.pagination.bullets && a.pagination.bullets.length && a.pagination.bullets.each(function (e, t) {
							t = (0, E.$)(t);
							a.a11y.makeElFocusable(t), a.a11y.addElRole(t, "button"), a.a11y.addElLabel(t, i.paginationBulletMessage.replace(/\{\{index\}\}/, t.index() + 1))
						})
					},
					init: function () {
						var e, t, a = this,
							i = (a.$el.append(a.a11y.liveRegion), a.params.a11y);
						a.navigation && a.navigation.$nextEl && (e = a.navigation.$nextEl), a.navigation && a.navigation.$prevEl && (t = a.navigation.$prevEl), e && (a.a11y.makeElFocusable(e), a.a11y.addElRole(e, "button"), a.a11y.addElLabel(e, i.nextSlideMessage), e.on("keydown", a.a11y.onEnterKey)), t && (a.a11y.makeElFocusable(t), a.a11y.addElRole(t, "button"), a.a11y.addElLabel(t, i.prevSlideMessage), t.on("keydown", a.a11y.onEnterKey)), a.pagination && a.params.pagination.clickable && a.pagination.bullets && a.pagination.bullets.length && a.pagination.$el.on("keydown", ".".concat(a.params.pagination.bulletClass), a.a11y.onEnterKey)
					},
					destroy: function () {
						var e, t, a = this;
						a.a11y.liveRegion && 0 < a.a11y.liveRegion.length && a.a11y.liveRegion.remove(), a.navigation && a.navigation.$nextEl && (e = a.navigation.$nextEl), a.navigation && a.navigation.$prevEl && (t = a.navigation.$prevEl), e && e.off("keydown", a.a11y.onEnterKey), t && t.off("keydown", a.a11y.onEnterKey), a.pagination && a.params.pagination.clickable && a.pagination.bullets && a.pagination.bullets.length && a.pagination.$el.off("keydown", ".".concat(a.params.pagination.bulletClass), a.a11y.onEnterKey)
					}
				},
				z = {
					init: function () {
						var e, t = this;
						t.params.history && (U.window.history && U.window.history.pushState ? ((e = t.history).initialized = !0, e.paths = z.getPathValues(), (e.paths.key || e.paths.value) && (e.scrollToSlide(0, e.paths.value, t.params.runCallbacksOnInit), t.params.history.replaceState || U.window.addEventListener("popstate", t.history.setHistoryPopState))) : (t.params.history.enabled = !1, t.params.hashNavigation.enabled = !0))
					},
					destroy: function () {
						this.params.history.replaceState || U.window.removeEventListener("popstate", this.history.setHistoryPopState)
					},
					setHistoryPopState: function () {
						this.history.paths = z.getPathValues(), this.history.scrollToSlide(this.params.speed, this.history.paths.value, !1)
					},
					getPathValues: function () {
						var e = U.window.location.pathname.slice(1).split("/").filter(function (e) {
								return "" !== e
							}),
							t = e.length;
						return {
							key: e[t - 2],
							value: e[t - 1]
						}
					},
					setHistory: function (e, t) {
						this.history.initialized && this.params.history.enabled && (t = this.slides.eq(t), t = z.slugify(t.attr("data-history")), U.window.location.pathname.includes(e) || (t = "".concat(e, "/").concat(t)), (e = U.window.history.state) && e.value === t || (this.params.history.replaceState ? U.window.history.replaceState({
							value: t
						}, null, t) : U.window.history.pushState({
							value: t
						}, null, t)))
					},
					slugify: function (e) {
						return e.toString().replace(/\s+/g, "-").replace(/[^\w-]+/g, "").replace(/--+/g, "-").replace(/^-+/, "").replace(/-+$/, "")
					},
					scrollToSlide: function (e, t, a) {
						var i = this;
						if (t)
							for (var n = 0, s = i.slides.length; n < s; n += 1) {
								var r = i.slides.eq(n);
								z.slugify(r.attr("data-history")) !== t || r.hasClass(i.params.slideDuplicateClass) || (r = r.index(), i.slideTo(r, e, a))
							} else i.slideTo(0, e, a)
					}
				},
				L = {
					onHashCange: function () {
						var e = this,
							t = U.document.location.hash.replace("#", "");
						t !== e.slides.eq(e.activeIndex).attr("data-hash") && void 0 !== (t = e.$wrapperEl.children(".".concat(e.params.slideClass, '[data-hash="').concat(t, '"]')).index()) && e.slideTo(t)
					},
					setHash: function () {
						var e = this;
						e.hashNavigation.initialized && e.params.hashNavigation.enabled && (e.params.hashNavigation.replaceState && U.window.history && U.window.history.replaceState ? U.window.history.replaceState(null, null, "#".concat(e.slides.eq(e.activeIndex).attr("data-hash")) || "") : (e = (e = e.slides.eq(e.activeIndex)).attr("data-hash") || e.attr("data-history"), U.document.location.hash = e || ""))
					},
					init: function () {
						var e = this;
						if (!(!e.params.hashNavigation.enabled || e.params.history && e.params.history.enabled)) {
							e.hashNavigation.initialized = !0;
							var t = U.document.location.hash.replace("#", "");
							if (t)
								for (var a = 0, i = e.slides.length; a < i; a += 1) {
									var n = e.slides.eq(a);
									(n.attr("data-hash") || n.attr("data-history")) !== t || n.hasClass(e.params.slideDuplicateClass) || (n = n.index(), e.slideTo(n, 0, e.params.runCallbacksOnInit, !0))
								}
							e.params.hashNavigation.watchState && (0, E.$)(U.window).on("hashchange", e.hashNavigation.onHashCange)
						}
					},
					destroy: function () {
						this.params.hashNavigation.watchState && (0, E.$)(U.window).off("hashchange", this.hashNavigation.onHashCange)
					}
				},
				I = {
					run: function () {
						var e = this,
							t = e.slides.eq(e.activeIndex),
							a = e.params.autoplay.delay;
						t.attr("data-swiper-autoplay") && (a = t.attr("data-swiper-autoplay") || e.params.autoplay.delay), clearTimeout(e.autoplay.timeout), e.autoplay.timeout = K.nextTick(function () {
							e.params.autoplay.reverseDirection ? e.params.loop ? (e.loopFix(), e.slidePrev(e.params.speed, !0, !0), e.emit("autoplay")) : e.isBeginning ? e.params.autoplay.stopOnLastSlide ? e.autoplay.stop() : (e.slideTo(e.slides.length - 1, e.params.speed, !0, !0), e.emit("autoplay")) : (e.slidePrev(e.params.speed, !0, !0), e.emit("autoplay")) : e.params.loop ? (e.loopFix(), e.slideNext(e.params.speed, !0, !0), e.emit("autoplay")) : e.isEnd ? e.params.autoplay.stopOnLastSlide ? e.autoplay.stop() : (e.slideTo(0, e.params.speed, !0, !0), e.emit("autoplay")) : (e.slideNext(e.params.speed, !0, !0), e.emit("autoplay")), e.params.cssMode && e.autoplay.running && e.autoplay.run()
						}, a)
					},
					start: function () {
						var e = this;
						return void 0 === e.autoplay.timeout && (!e.autoplay.running && (e.autoplay.running = !0, e.emit("autoplayStart"), e.autoplay.run(), !0))
					},
					stop: function () {
						var e = this;
						return !!e.autoplay.running && (void 0 !== e.autoplay.timeout && (e.autoplay.timeout && (clearTimeout(e.autoplay.timeout), e.autoplay.timeout = void 0), e.autoplay.running = !1, e.emit("autoplayStop"), !0))
					},
					pause: function (e) {
						var t = this;
						t.autoplay.running && !t.autoplay.paused && (t.autoplay.timeout && clearTimeout(t.autoplay.timeout), t.autoplay.paused = !0, 0 !== e && t.params.autoplay.waitForTransition ? (t.$wrapperEl[0].addEventListener("transitionend", t.autoplay.onTransitionEnd), t.$wrapperEl[0].addEventListener("webkitTransitionEnd", t.autoplay.onTransitionEnd)) : (t.autoplay.paused = !1, t.autoplay.run()))
					}
				},
				Q = {
					setTranslate: function () {
						for (var e = this, t = e.slides, a = 0; a < t.length; a += 1) {
							var i = e.slides.eq(a),
								n = -i[0].swiperSlideOffset,
								s = (e.params.virtualTranslate || (n -= e.translate), 0),
								r = (e.isHorizontal() || (s = n, n = 0), e.params.fadeEffect.crossFade ? Math.max(1 - Math.abs(i[0].progress), 0) : 1 + Math.min(Math.max(i[0].progress, -1), 0));
							i.css({
								opacity: r
							}).transform("translate3d(".concat(n, "px, ").concat(s, "px, 0px)"))
						}
					},
					setTransition: function (e) {
						var a, i = this,
							t = i.slides,
							n = i.$wrapperEl;
						t.transition(e), i.params.virtualTranslate && 0 !== e && (a = !1, t.transitionEnd(function () {
							if (!a && i && !i.destroyed) {
								a = !0, i.animating = !1;
								for (var e = ["webkitTransitionEnd", "transitionend"], t = 0; t < e.length; t += 1) n.trigger(e[t])
							}
						}))
					}
				},
				J = {
					setTranslate: function () {
						var e, t = this,
							a = t.$el,
							i = t.$wrapperEl,
							n = t.slides,
							s = t.width,
							r = t.height,
							o = t.rtlTranslate,
							l = t.size,
							d = t.params.cubeEffect,
							c = t.isHorizontal(),
							u = t.virtual && t.params.virtual.enabled,
							p = 0;
						d.shadow && (c ? (0 === (e = i.find(".swiper-cube-shadow")).length && (e = (0, E.$)('<div class="swiper-cube-shadow"></div>'), i.append(e)), e.css({
							height: "".concat(s, "px")
						})) : 0 === (e = a.find(".swiper-cube-shadow")).length && (e = (0, E.$)('<div class="swiper-cube-shadow"></div>'), a.append(e)));
						for (var h, f = 0; f < n.length; f += 1) {
							var m = n.eq(f),
								v = f,
								g = 90 * (v = u ? parseInt(m.attr("data-swiper-slide-index"), 10) : v),
								w = Math.floor(g / 360),
								y = (o && (g = -g, w = Math.floor(-g / 360)), Math.max(Math.min(m[0].progress, 1), -1)),
								b = 0,
								x = 0,
								T = 0,
								w = (v % 4 == 0 ? (b = 4 * -w * l, T = 0) : (v - 1) % 4 == 0 ? (b = 0, T = 4 * -w * l) : (v - 2) % 4 == 0 ? (b = l + 4 * w * l, T = l) : (v - 3) % 4 == 0 && (b = -l, T = 3 * l + 4 * l * w), o && (b = -b), c || (x = b, b = 0), "rotateX(".concat(c ? 0 : -g, "deg) rotateY(").concat(c ? g : 0, "deg) translate3d(").concat(b, "px, ").concat(x, "px, ").concat(T, "px)"));
							y <= 1 && -1 < y && (p = o ? 90 * -v - 90 * y : 90 * v + 90 * y), m.transform(w), d.slideShadows && (g = c ? m.find(".swiper-slide-shadow-left") : m.find(".swiper-slide-shadow-top"), b = c ? m.find(".swiper-slide-shadow-right") : m.find(".swiper-slide-shadow-bottom"), 0 === g.length && (g = (0, E.$)('<div class="swiper-slide-shadow-'.concat(c ? "left" : "top", '"></div>')), m.append(g)), 0 === b.length && (b = (0, E.$)('<div class="swiper-slide-shadow-'.concat(c ? "right" : "bottom", '"></div>')), m.append(b)), g.length && (g[0].style.opacity = Math.max(-y, 0)), b.length && (b[0].style.opacity = Math.max(y, 0)))
						}
						i.css({
							"-webkit-transform-origin": "50% 50% -".concat(l / 2, "px"),
							"-moz-transform-origin": "50% 50% -".concat(l / 2, "px"),
							"-ms-transform-origin": "50% 50% -".concat(l / 2, "px"),
							"transform-origin": "50% 50% -".concat(l / 2, "px")
						}), d.shadow && (c ? e.transform("translate3d(0px, ".concat(s / 2 + d.shadowOffset, "px, ").concat(-s / 2, "px) rotateX(90deg) rotateZ(0deg) scale(").concat(d.shadowScale, ")")) : (a = Math.abs(p) - 90 * Math.floor(Math.abs(p) / 90), s = 1.5 - (Math.sin(2 * a * Math.PI / 360) / 2 + Math.cos(2 * a * Math.PI / 360) / 2), a = d.shadowScale, s = d.shadowScale / s, h = d.shadowOffset, e.transform("scale3d(".concat(a, ", 1, ").concat(s, ") translate3d(0px, ").concat(r / 2 + h, "px, ").concat(-r / 2 / s, "px) rotateX(-90deg)")))), i.transform("translate3d(0px,0,".concat(S.isSafari || S.isUiWebView ? -l / 2 : 0, "px) rotateX(").concat(t.isHorizontal() ? 0 : p, "deg) rotateY(").concat(t.isHorizontal() ? -p : 0, "deg)"))
					},
					setTransition: function (e) {
						var t = this.$el;
						this.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e), this.params.cubeEffect.shadow && !this.isHorizontal() && t.find(".swiper-cube-shadow").transition(e)
					}
				},
				ee = {
					setTranslate: function () {
						for (var e = this, t = e.slides, a = e.rtlTranslate, i = 0; i < t.length; i += 1) {
							var n, s, r = t.eq(i),
								o = r[0].progress,
								l = (e.params.flipEffect.limitRotation && (o = Math.max(Math.min(r[0].progress, 1), -1)), r[0].swiperSlideOffset),
								d = -180 * o,
								c = 0,
								l = -l,
								u = 0;
							e.isHorizontal() ? a && (d = -d) : (u = l, c = -d, d = l = 0), r[0].style.zIndex = -Math.abs(Math.round(o)) + t.length, e.params.flipEffect.slideShadows && (n = e.isHorizontal() ? r.find(".swiper-slide-shadow-left") : r.find(".swiper-slide-shadow-top"), s = e.isHorizontal() ? r.find(".swiper-slide-shadow-right") : r.find(".swiper-slide-shadow-bottom"), 0 === n.length && (n = (0, E.$)('<div class="swiper-slide-shadow-'.concat(e.isHorizontal() ? "left" : "top", '"></div>')), r.append(n)), 0 === s.length && (s = (0, E.$)('<div class="swiper-slide-shadow-'.concat(e.isHorizontal() ? "right" : "bottom", '"></div>')), r.append(s)), n.length && (n[0].style.opacity = Math.max(-o, 0)), s.length && (s[0].style.opacity = Math.max(o, 0))), r.transform("translate3d(".concat(l, "px, ").concat(u, "px, 0px) rotateX(").concat(c, "deg) rotateY(").concat(d, "deg)"))
						}
					},
					setTransition: function (e) {
						var a, i = this,
							t = i.slides,
							n = i.activeIndex,
							s = i.$wrapperEl;
						t.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e), i.params.virtualTranslate && 0 !== e && (a = !1, t.eq(n).transitionEnd(function () {
							if (!a && i && !i.destroyed) {
								a = !0, i.animating = !1;
								for (var e = ["webkitTransitionEnd", "transitionend"], t = 0; t < e.length; t += 1) s.trigger(e[t])
							}
						}))
					}
				},
				te = {
					setTranslate: function () {
						for (var e = this, t = e.width, a = e.height, i = e.slides, n = e.$wrapperEl, s = e.slidesSizesGrid, r = e.params.coverflowEffect, o = e.isHorizontal(), e = e.translate, l = o ? t / 2 - e : a / 2 - e, d = o ? r.rotate : -r.rotate, c = r.depth, u = 0, p = i.length; u < p; u += 1) {
							var h = i.eq(u),
								f = s[u],
								m = (l - h[0].swiperSlideOffset - f / 2) / f * r.modifier,
								v = o ? d * m : 0,
								g = o ? 0 : d * m,
								w = -c * Math.abs(m),
								y = r.stretch,
								f = ("string" == typeof y && -1 !== y.indexOf("%") && (y = parseFloat(r.stretch) / 100 * f), o ? 0 : y * m),
								y = o ? y * m : 0,
								y = (Math.abs(y) < .001 && (y = 0), Math.abs(f) < .001 && (f = 0), Math.abs(w) < .001 && (w = 0), Math.abs(v) < .001 && (v = 0), Math.abs(g) < .001 && (g = 0), "translate3d(".concat(y, "px,").concat(f, "px,").concat(w, "px)  rotateX(").concat(g, "deg) rotateY(").concat(v, "deg)"));
							h.transform(y), h[0].style.zIndex = 1 - Math.abs(Math.round(m)), r.slideShadows && (f = o ? h.find(".swiper-slide-shadow-left") : h.find(".swiper-slide-shadow-top"), w = o ? h.find(".swiper-slide-shadow-right") : h.find(".swiper-slide-shadow-bottom"), 0 === f.length && (f = (0, E.$)('<div class="swiper-slide-shadow-'.concat(o ? "left" : "top", '"></div>')), h.append(f)), 0 === w.length && (w = (0, E.$)('<div class="swiper-slide-shadow-'.concat(o ? "right" : "bottom", '"></div>')), h.append(w)), f.length && (f[0].style.opacity = 0 < m ? m : 0), w.length && (w[0].style.opacity = 0 < -m ? -m : 0))
						}(b.pointerEvents || b.prefixedPointerEvents) && (n[0].style.perspectiveOrigin = "".concat(l, "px 50%"))
					},
					setTransition: function (e) {
						this.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e)
					}
				},
				ae = {
					init: function () {
						var e = this,
							t = e.params.thumbs,
							a = e.constructor;
						t.swiper instanceof a ? (e.thumbs.swiper = t.swiper, K.extend(e.thumbs.swiper.originalParams, {
							watchSlidesProgress: !0,
							slideToClickedSlide: !1
						}), K.extend(e.thumbs.swiper.params, {
							watchSlidesProgress: !0,
							slideToClickedSlide: !1
						})) : K.isObject(t.swiper) && (e.thumbs.swiper = new a(K.extend({}, t.swiper, {
							watchSlidesVisibility: !0,
							watchSlidesProgress: !0,
							slideToClickedSlide: !1
						})), e.thumbs.swiperCreated = !0), e.thumbs.swiper.$el.addClass(e.params.thumbs.thumbsContainerClass), e.thumbs.swiper.on("tap", e.thumbs.onThumbClick)
					},
					onThumbClick: function () {
						var e, t, a, i = this,
							n = i.thumbs.swiper;
						n && (e = n.clickedIndex, (a = n.clickedSlide) && (0, E.$)(a).hasClass(i.params.thumbs.slideThumbActiveClass) || null != e && (a = n.params.loop ? parseInt((0, E.$)(n.clickedSlide).attr("data-swiper-slide-index"), 10) : e, i.params.loop && (n = i.activeIndex, i.slides.eq(n).hasClass(i.params.slideDuplicateClass) && (i.loopFix(), i._clientLeft = i.$wrapperEl[0].clientLeft, n = i.activeIndex), e = i.slides.eq(n).prevAll('[data-swiper-slide-index="'.concat(a, '"]')).eq(0).index(), t = i.slides.eq(n).nextAll('[data-swiper-slide-index="'.concat(a, '"]')).eq(0).index(), a = void 0 === e || void 0 !== t && t - n < n - e ? t : e), i.slideTo(a)))
					},
					update: function (e) {
						var t = this,
							a = t.thumbs.swiper;
						if (a) {
							var i, n, s, r = "auto" === a.params.slidesPerView ? a.slidesPerViewDynamic() : a.params.slidesPerView,
								o = t.params.thumbs.autoScrollOffset,
								l = o && !a.params.loop,
								d = (t.realIndex === a.realIndex && !l || (i = a.activeIndex, s = a.params.loop ? (a.slides.eq(i).hasClass(a.params.slideDuplicateClass) && (a.loopFix(), a._clientLeft = a.$wrapperEl[0].clientLeft, i = a.activeIndex), s = a.slides.eq(i).prevAll('[data-swiper-slide-index="'.concat(t.realIndex, '"]')).eq(0).index(), n = a.slides.eq(i).nextAll('[data-swiper-slide-index="'.concat(t.realIndex, '"]')).eq(0).index(), n = void 0 === s ? n : void 0 === n ? s : n - i == i - s ? i : n - i < i - s ? n : s, t.activeIndex > t.previousIndex ? "next" : "prev") : (n = t.realIndex) > t.previousIndex ? "next" : "prev", l && (n += "next" === s ? o : -1 * o), a.visibleSlidesIndexes && a.visibleSlidesIndexes.indexOf(n) < 0 && (a.params.centeredSlides ? n = i < n ? n - Math.floor(r / 2) + 1 : n + Math.floor(r / 2) - 1 : i < n && (n = n - r + 1), a.slideTo(n, e ? 0 : void 0))), 1),
								c = t.params.thumbs.slideThumbActiveClass;
							if (1 < t.params.slidesPerView && !t.params.centeredSlides && (d = t.params.slidesPerView), t.params.thumbs.multipleActiveThumbs || (d = 1), d = Math.floor(d), a.slides.removeClass(c), a.params.loop || a.params.virtual && a.params.virtual.enabled)
								for (var u = 0; u < d; u += 1) a.$wrapperEl.children('[data-swiper-slide-index="'.concat(t.realIndex + u, '"]')).addClass(c);
							else
								for (var p = 0; p < d; p += 1) a.slides.eq(t.realIndex + p).addClass(c)
						}
					}
				},
				O = [u, p, m, v, w, x, T, {
					name: "mousewheel",
					params: {
						mousewheel: {
							enabled: !1,
							releaseOnEdges: !1,
							invert: !1,
							forceToAxis: !1,
							sensitivity: 1,
							eventsTarged: "container"
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							mousewheel: {
								enabled: !1,
								enable: C.enable.bind(e),
								disable: C.disable.bind(e),
								handle: C.handle.bind(e),
								handleMouseEnter: C.handleMouseEnter.bind(e),
								handleMouseLeave: C.handleMouseLeave.bind(e),
								animateSlider: C.animateSlider.bind(e),
								releaseScroll: C.releaseScroll.bind(e),
								lastScrollTime: K.now(),
								lastEventBeforeSnap: void 0,
								recentWheelEvents: []
							}
						})
					},
					on: {
						init: function () {
							var e = this;
							!e.params.mousewheel.enabled && e.params.cssMode && e.mousewheel.disable(), e.params.mousewheel.enabled && e.mousewheel.enable()
						},
						destroy: function () {
							this.params.cssMode && this.mousewheel.enable(), this.mousewheel.enabled && this.mousewheel.disable()
						}
					}
				}, {
					name: "navigation",
					params: {
						navigation: {
							nextEl: null,
							prevEl: null,
							hideOnClick: !1,
							disabledClass: "swiper-button-disabled",
							hiddenClass: "swiper-button-hidden",
							lockClass: "swiper-button-lock"
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							navigation: {
								init: M.init.bind(e),
								update: M.update.bind(e),
								destroy: M.destroy.bind(e),
								onNextClick: M.onNextClick.bind(e),
								onPrevClick: M.onPrevClick.bind(e)
							}
						})
					},
					on: {
						init: function () {
							this.navigation.init(), this.navigation.update()
						},
						toEdge: function () {
							this.navigation.update()
						},
						fromEdge: function () {
							this.navigation.update()
						},
						destroy: function () {
							this.navigation.destroy()
						},
						click: function (e) {
							var t, a = this,
								i = a.navigation,
								n = i.$nextEl,
								i = i.$prevEl;
							!a.params.navigation.hideOnClick || (0, E.$)(e.target).is(i) || (0, E.$)(e.target).is(n) || (n ? t = n.hasClass(a.params.navigation.hiddenClass) : i && (t = i.hasClass(a.params.navigation.hiddenClass)), !0 === t ? a.emit("navigationShow", a) : a.emit("navigationHide", a), n && n.toggleClass(a.params.navigation.hiddenClass), i && i.toggleClass(a.params.navigation.hiddenClass))
						}
					}
				}, {
					name: "pagination",
					params: {
						pagination: {
							el: null,
							bulletElement: "span",
							clickable: !1,
							hideOnClick: !1,
							renderBullet: null,
							renderProgressbar: null,
							renderFraction: null,
							renderCustom: null,
							progressbarOpposite: !1,
							type: "bullets",
							dynamicBullets: !1,
							dynamicMainBullets: 1,
							formatFractionCurrent: function (e) {
								return e
							},
							formatFractionTotal: function (e) {
								return e
							},
							bulletClass: "swiper-pagination-bullet",
							bulletActiveClass: "swiper-pagination-bullet-active",
							modifierClass: "swiper-pagination-",
							currentClass: "swiper-pagination-current",
							totalClass: "swiper-pagination-total",
							hiddenClass: "swiper-pagination-hidden",
							progressbarFillClass: "swiper-pagination-progressbar-fill",
							progressbarOppositeClass: "swiper-pagination-progressbar-opposite",
							clickableClass: "swiper-pagination-clickable",
							lockClass: "swiper-pagination-lock"
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							pagination: {
								init: P.init.bind(e),
								render: P.render.bind(e),
								update: P.update.bind(e),
								destroy: P.destroy.bind(e),
								dynamicBulletIndex: 0
							}
						})
					},
					on: {
						init: function () {
							this.pagination.init(), this.pagination.render(), this.pagination.update()
						},
						activeIndexChange: function () {
							!this.params.loop && void 0 !== this.snapIndex || this.pagination.update()
						},
						snapIndexChange: function () {
							this.params.loop || this.pagination.update()
						},
						slidesLengthChange: function () {
							this.params.loop && (this.pagination.render(), this.pagination.update())
						},
						snapGridLengthChange: function () {
							this.params.loop || (this.pagination.render(), this.pagination.update())
						},
						destroy: function () {
							this.pagination.destroy()
						},
						click: function (e) {
							var t = this;
							t.params.pagination.el && t.params.pagination.hideOnClick && 0 < t.pagination.$el.length && !(0, E.$)(e.target).hasClass(t.params.pagination.bulletClass) && (!0 === t.pagination.$el.hasClass(t.params.pagination.hiddenClass) ? t.emit("paginationShow", t) : t.emit("paginationHide", t), t.pagination.$el.toggleClass(t.params.pagination.hiddenClass))
						}
					}
				}, {
					name: "scrollbar",
					params: {
						scrollbar: {
							el: null,
							dragSize: "auto",
							hide: !1,
							draggable: !1,
							snapOnRelease: !0,
							lockClass: "swiper-scrollbar-lock",
							dragClass: "swiper-scrollbar-drag"
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							scrollbar: {
								init: k.init.bind(e),
								destroy: k.destroy.bind(e),
								updateSize: k.updateSize.bind(e),
								setTranslate: k.setTranslate.bind(e),
								setTransition: k.setTransition.bind(e),
								enableDraggable: k.enableDraggable.bind(e),
								disableDraggable: k.disableDraggable.bind(e),
								setDragPosition: k.setDragPosition.bind(e),
								getPointerPosition: k.getPointerPosition.bind(e),
								onDragStart: k.onDragStart.bind(e),
								onDragMove: k.onDragMove.bind(e),
								onDragEnd: k.onDragEnd.bind(e),
								isTouched: !1,
								timeout: null,
								dragTimeout: null
							}
						})
					},
					on: {
						init: function () {
							this.scrollbar.init(), this.scrollbar.updateSize(), this.scrollbar.setTranslate()
						},
						update: function () {
							this.scrollbar.updateSize()
						},
						resize: function () {
							this.scrollbar.updateSize()
						},
						observerUpdate: function () {
							this.scrollbar.updateSize()
						},
						setTranslate: function () {
							this.scrollbar.setTranslate()
						},
						setTransition: function (e) {
							this.scrollbar.setTransition(e)
						},
						destroy: function () {
							this.scrollbar.destroy()
						}
					}
				}, {
					name: "parallax",
					params: {
						parallax: {
							enabled: !1
						}
					},
					create: function () {
						K.extend(this, {
							parallax: {
								setTransform: j.setTransform.bind(this),
								setTranslate: j.setTranslate.bind(this),
								setTransition: j.setTransition.bind(this)
							}
						})
					},
					on: {
						beforeInit: function () {
							this.params.parallax.enabled && (this.params.watchSlidesProgress = !0, this.originalParams.watchSlidesProgress = !0)
						},
						init: function () {
							this.params.parallax.enabled && this.parallax.setTranslate()
						},
						setTranslate: function () {
							this.params.parallax.enabled && this.parallax.setTranslate()
						},
						setTransition: function (e) {
							this.params.parallax.enabled && this.parallax.setTransition(e)
						}
					}
				}, {
					name: "zoom",
					params: {
						zoom: {
							enabled: !1,
							maxRatio: 3,
							minRatio: 1,
							toggle: !0,
							containerClass: "swiper-zoom-container",
							zoomedSlideClass: "swiper-slide-zoomed"
						}
					},
					create: function () {
						var i = this,
							t = {
								enabled: !1,
								scale: 1,
								currentScale: 1,
								isScaling: !1,
								gesture: {
									$slideEl: void 0,
									slideWidth: void 0,
									slideHeight: void 0,
									$imageEl: void 0,
									$imageWrapEl: void 0,
									maxRatio: 3
								},
								image: {
									isTouched: void 0,
									isMoved: void 0,
									currentX: void 0,
									currentY: void 0,
									minX: void 0,
									minY: void 0,
									maxX: void 0,
									maxY: void 0,
									width: void 0,
									height: void 0,
									startX: void 0,
									startY: void 0,
									touchesStart: {},
									touchesCurrent: {}
								},
								velocity: {
									x: void 0,
									y: void 0,
									prevPositionX: void 0,
									prevPositionY: void 0,
									prevTime: void 0
								}
							},
							n = ("onGestureStart onGestureChange onGestureEnd onTouchStart onTouchMove onTouchEnd onTransitionEnd toggle enable disable in out".split(" ").forEach(function (e) {
								t[e] = W[e].bind(i)
							}), K.extend(i, {
								zoom: t
							}), 1);
						Object.defineProperty(i.zoom, "scale", {
							get: function () {
								return n
							},
							set: function (e) {
								var t, a;
								n !== e && (t = i.zoom.gesture.$imageEl ? i.zoom.gesture.$imageEl[0] : void 0, a = i.zoom.gesture.$slideEl ? i.zoom.gesture.$slideEl[0] : void 0, i.emit("zoomChange", e, t, a)), n = e
							}
						})
					},
					on: {
						init: function () {
							this.params.zoom.enabled && this.zoom.enable()
						},
						destroy: function () {
							this.zoom.disable()
						},
						touchStart: function (e) {
							this.zoom.enabled && this.zoom.onTouchStart(e)
						},
						touchEnd: function (e) {
							this.zoom.enabled && this.zoom.onTouchEnd(e)
						},
						doubleTap: function (e) {
							this.params.zoom.enabled && this.zoom.enabled && this.params.zoom.toggle && this.zoom.toggle(e)
						},
						transitionEnd: function () {
							this.zoom.enabled && this.params.zoom.enabled && this.zoom.onTransitionEnd()
						},
						slideChange: function () {
							this.zoom.enabled && this.params.zoom.enabled && this.params.cssMode && this.zoom.onTransitionEnd()
						}
					}
				}, {
					name: "lazy",
					params: {
						lazy: {
							enabled: !1,
							loadPrevNext: !1,
							loadPrevNextAmount: 1,
							loadOnTransitionStart: !1,
							elementClass: "swiper-lazy",
							loadingClass: "swiper-lazy-loading",
							loadedClass: "swiper-lazy-loaded",
							preloaderClass: "swiper-lazy-preloader"
						}
					},
					create: function () {
						K.extend(this, {
							lazy: {
								initialImageLoaded: !1,
								load: _.load.bind(this),
								loadInSlide: _.loadInSlide.bind(this)
							}
						})
					},
					on: {
						beforeInit: function () {
							this.params.lazy.enabled && this.params.preloadImages && (this.params.preloadImages = !1)
						},
						init: function () {
							this.params.lazy.enabled && !this.params.loop && 0 === this.params.initialSlide && this.lazy.load()
						},
						scroll: function () {
							this.params.freeMode && !this.params.freeModeSticky && this.lazy.load()
						},
						resize: function () {
							this.params.lazy.enabled && this.lazy.load()
						},
						scrollbarDragMove: function () {
							this.params.lazy.enabled && this.lazy.load()
						},
						transitionStart: function () {
							var e = this;
							e.params.lazy.enabled && (e.params.lazy.loadOnTransitionStart || !e.params.lazy.loadOnTransitionStart && !e.lazy.initialImageLoaded) && e.lazy.load()
						},
						transitionEnd: function () {
							this.params.lazy.enabled && !this.params.lazy.loadOnTransitionStart && this.lazy.load()
						},
						slideChange: function () {
							this.params.lazy.enabled && this.params.cssMode && this.lazy.load()
						}
					}
				}, {
					name: "controller",
					params: {
						controller: {
							control: void 0,
							inverse: !1,
							by: "slide"
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							controller: {
								control: e.params.controller.control,
								getInterpolateFunction: $.getInterpolateFunction.bind(e),
								setTranslate: $.setTranslate.bind(e),
								setTransition: $.setTransition.bind(e)
							}
						})
					},
					on: {
						update: function () {
							this.controller.control && this.controller.spline && (this.controller.spline = void 0, delete this.controller.spline)
						},
						resize: function () {
							this.controller.control && this.controller.spline && (this.controller.spline = void 0, delete this.controller.spline)
						},
						observerUpdate: function () {
							this.controller.control && this.controller.spline && (this.controller.spline = void 0, delete this.controller.spline)
						},
						setTranslate: function (e, t) {
							this.controller.control && this.controller.setTranslate(e, t)
						},
						setTransition: function (e, t) {
							this.controller.control && this.controller.setTransition(e, t)
						}
					}
				}, {
					name: "a11y",
					params: {
						a11y: {
							enabled: !0,
							notificationClass: "swiper-notification",
							prevSlideMessage: "Previous slide",
							nextSlideMessage: "Next slide",
							firstSlideMessage: "This is the first slide",
							lastSlideMessage: "This is the last slide",
							paginationBulletMessage: "Go to slide {{index}}"
						}
					},
					create: function () {
						var t = this;
						K.extend(t, {
							a11y: {
								liveRegion: (0, E.$)('<span class="'.concat(t.params.a11y.notificationClass, '" aria-live="assertive" aria-atomic="true"></span>'))
							}
						}), Object.keys(Z).forEach(function (e) {
							t.a11y[e] = Z[e].bind(t)
						})
					},
					on: {
						init: function () {
							this.params.a11y.enabled && (this.a11y.init(), this.a11y.updateNavigation())
						},
						toEdge: function () {
							this.params.a11y.enabled && this.a11y.updateNavigation()
						},
						fromEdge: function () {
							this.params.a11y.enabled && this.a11y.updateNavigation()
						},
						paginationUpdate: function () {
							this.params.a11y.enabled && this.a11y.updatePagination()
						},
						destroy: function () {
							this.params.a11y.enabled && this.a11y.destroy()
						}
					}
				}, {
					name: "history",
					params: {
						history: {
							enabled: !1,
							replaceState: !1,
							key: "slides"
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							history: {
								init: z.init.bind(e),
								setHistory: z.setHistory.bind(e),
								setHistoryPopState: z.setHistoryPopState.bind(e),
								scrollToSlide: z.scrollToSlide.bind(e),
								destroy: z.destroy.bind(e)
							}
						})
					},
					on: {
						init: function () {
							this.params.history.enabled && this.history.init()
						},
						destroy: function () {
							this.params.history.enabled && this.history.destroy()
						},
						transitionEnd: function () {
							this.history.initialized && this.history.setHistory(this.params.history.key, this.activeIndex)
						},
						slideChange: function () {
							var e = this;
							e.history.initialized && e.params.cssMode && e.history.setHistory(e.params.history.key, e.activeIndex)
						}
					}
				}, {
					name: "hash-navigation",
					params: {
						hashNavigation: {
							enabled: !1,
							replaceState: !1,
							watchState: !1
						}
					},
					create: function () {
						var e = this;
						K.extend(e, {
							hashNavigation: {
								initialized: !1,
								init: L.init.bind(e),
								destroy: L.destroy.bind(e),
								setHash: L.setHash.bind(e),
								onHashCange: L.onHashCange.bind(e)
							}
						})
					},
					on: {
						init: function () {
							this.params.hashNavigation.enabled && this.hashNavigation.init()
						},
						destroy: function () {
							this.params.hashNavigation.enabled && this.hashNavigation.destroy()
						},
						transitionEnd: function () {
							this.hashNavigation.initialized && this.hashNavigation.setHash()
						},
						slideChange: function () {
							this.hashNavigation.initialized && this.params.cssMode && this.hashNavigation.setHash()
						}
					}
				}, {
					name: "autoplay",
					params: {
						autoplay: {
							enabled: !1,
							delay: 3e3,
							waitForTransition: !0,
							disableOnInteraction: !0,
							stopOnLastSlide: !1,
							reverseDirection: !1
						}
					},
					create: function () {
						var t = this;
						K.extend(t, {
							autoplay: {
								running: !1,
								paused: !1,
								run: I.run.bind(t),
								start: I.start.bind(t),
								stop: I.stop.bind(t),
								pause: I.pause.bind(t),
								onVisibilityChange: function () {
									"hidden" === document.visibilityState && t.autoplay.running && t.autoplay.pause(), "visible" === document.visibilityState && t.autoplay.paused && (t.autoplay.run(), t.autoplay.paused = !1)
								},
								onTransitionEnd: function (e) {
									t && !t.destroyed && t.$wrapperEl && e.target === this && (t.$wrapperEl[0].removeEventListener("transitionend", t.autoplay.onTransitionEnd), t.$wrapperEl[0].removeEventListener("webkitTransitionEnd", t.autoplay.onTransitionEnd), t.autoplay.paused = !1, t.autoplay.running ? t.autoplay.run() : t.autoplay.stop())
								}
							}
						})
					},
					on: {
						init: function () {
							this.params.autoplay.enabled && (this.autoplay.start(), document.addEventListener("visibilitychange", this.autoplay.onVisibilityChange))
						},
						beforeTransitionStart: function (e, t) {
							this.autoplay.running && (t || !this.params.autoplay.disableOnInteraction ? this.autoplay.pause(e) : this.autoplay.stop())
						},
						sliderFirstMove: function () {
							this.autoplay.running && (this.params.autoplay.disableOnInteraction ? this.autoplay.stop() : this.autoplay.pause())
						},
						touchEnd: function () {
							this.params.cssMode && this.autoplay.paused && !this.params.autoplay.disableOnInteraction && this.autoplay.run()
						},
						destroy: function () {
							this.autoplay.running && this.autoplay.stop(), document.removeEventListener("visibilitychange", this.autoplay.onVisibilityChange)
						}
					}
				}, {
					name: "effect-fade",
					params: {
						fadeEffect: {
							crossFade: !1
						}
					},
					create: function () {
						K.extend(this, {
							fadeEffect: {
								setTranslate: Q.setTranslate.bind(this),
								setTransition: Q.setTransition.bind(this)
							}
						})
					},
					on: {
						beforeInit: function () {
							var e, t = this;
							"fade" === t.params.effect && (t.classNames.push("".concat(t.params.containerModifierClass, "fade")), K.extend(t.params, e = {
								slidesPerView: 1,
								slidesPerColumn: 1,
								slidesPerGroup: 1,
								watchSlidesProgress: !0,
								spaceBetween: 0,
								virtualTranslate: !0
							}), K.extend(t.originalParams, e))
						},
						setTranslate: function () {
							"fade" === this.params.effect && this.fadeEffect.setTranslate()
						},
						setTransition: function (e) {
							"fade" === this.params.effect && this.fadeEffect.setTransition(e)
						}
					}
				}, {
					name: "effect-cube",
					params: {
						cubeEffect: {
							slideShadows: !0,
							shadow: !0,
							shadowOffset: 20,
							shadowScale: .94
						}
					},
					create: function () {
						K.extend(this, {
							cubeEffect: {
								setTranslate: J.setTranslate.bind(this),
								setTransition: J.setTransition.bind(this)
							}
						})
					},
					on: {
						beforeInit: function () {
							var e, t = this;
							"cube" === t.params.effect && (t.classNames.push("".concat(t.params.containerModifierClass, "cube")), t.classNames.push("".concat(t.params.containerModifierClass, "3d")), K.extend(t.params, e = {
								slidesPerView: 1,
								slidesPerColumn: 1,
								slidesPerGroup: 1,
								watchSlidesProgress: !0,
								resistanceRatio: 0,
								spaceBetween: 0,
								centeredSlides: !1,
								virtualTranslate: !0
							}), K.extend(t.originalParams, e))
						},
						setTranslate: function () {
							"cube" === this.params.effect && this.cubeEffect.setTranslate()
						},
						setTransition: function (e) {
							"cube" === this.params.effect && this.cubeEffect.setTransition(e)
						}
					}
				}, {
					name: "effect-flip",
					params: {
						flipEffect: {
							slideShadows: !0,
							limitRotation: !0
						}
					},
					create: function () {
						K.extend(this, {
							flipEffect: {
								setTranslate: ee.setTranslate.bind(this),
								setTransition: ee.setTransition.bind(this)
							}
						})
					},
					on: {
						beforeInit: function () {
							var e, t = this;
							"flip" === t.params.effect && (t.classNames.push("".concat(t.params.containerModifierClass, "flip")), t.classNames.push("".concat(t.params.containerModifierClass, "3d")), K.extend(t.params, e = {
								slidesPerView: 1,
								slidesPerColumn: 1,
								slidesPerGroup: 1,
								watchSlidesProgress: !0,
								spaceBetween: 0,
								virtualTranslate: !0
							}), K.extend(t.originalParams, e))
						},
						setTranslate: function () {
							"flip" === this.params.effect && this.flipEffect.setTranslate()
						},
						setTransition: function (e) {
							"flip" === this.params.effect && this.flipEffect.setTransition(e)
						}
					}
				}, {
					name: "effect-coverflow",
					params: {
						coverflowEffect: {
							rotate: 50,
							stretch: 0,
							depth: 100,
							modifier: 1,
							slideShadows: !0
						}
					},
					create: function () {
						K.extend(this, {
							coverflowEffect: {
								setTranslate: te.setTranslate.bind(this),
								setTransition: te.setTransition.bind(this)
							}
						})
					},
					on: {
						beforeInit: function () {
							var e = this;
							"coverflow" === e.params.effect && (e.classNames.push("".concat(e.params.containerModifierClass, "coverflow")), e.classNames.push("".concat(e.params.containerModifierClass, "3d")), e.params.watchSlidesProgress = !0, e.originalParams.watchSlidesProgress = !0)
						},
						setTranslate: function () {
							"coverflow" === this.params.effect && this.coverflowEffect.setTranslate()
						},
						setTransition: function (e) {
							"coverflow" === this.params.effect && this.coverflowEffect.setTransition(e)
						}
					}
				}, {
					name: "thumbs",
					params: {
						thumbs: {
							swiper: null,
							multipleActiveThumbs: !0,
							autoScrollOffset: 0,
							slideThumbActiveClass: "swiper-slide-thumb-active",
							thumbsContainerClass: "swiper-container-thumbs"
						}
					},
					create: function () {
						K.extend(this, {
							thumbs: {
								swiper: null,
								init: ae.init.bind(this),
								update: ae.update.bind(this),
								onThumbClick: ae.onThumbClick.bind(this)
							}
						})
					},
					on: {
						beforeInit: function () {
							var e = this.params.thumbs;
							e && e.swiper && (this.thumbs.init(), this.thumbs.update(!0))
						},
						slideChange: function () {
							this.thumbs.swiper && this.thumbs.update()
						},
						update: function () {
							this.thumbs.swiper && this.thumbs.update()
						},
						resize: function () {
							this.thumbs.swiper && this.thumbs.update()
						},
						observerUpdate: function () {
							this.thumbs.swiper && this.thumbs.update()
						},
						setTransition: function (e) {
							var t = this.thumbs.swiper;
							t && t.setTransition(e)
						},
						beforeDestroy: function () {
							var e = this.thumbs.swiper;
							e && this.thumbs.swiperCreated && e && e.destroy()
						}
					}
				}],
				ie = (void 0 === c.use && (c.use = c.Class.use, c.installModule = c.Class.installModule), c.use(O), c)
		}, function (N, e, t) {
			t.r(e), t.d(e, {
				$: function () {
					return m
				},
				add: function () {
					return pe
				},
				addClass: function () {
					return a
				},
				animate: function () {
					return ge
				},
				animationEnd: function () {
					return C
				},
				append: function () {
					return _
				},
				appendTo: function () {
					return U
				},
				attr: function () {
					return o
				},
				blur: function () {
					return xe
				},
				change: function () {
					return $e
				},
				children: function () {
					return de
				},
				click: function () {
					return be
				},
				closest: function () {
					return oe
				},
				css: function () {
					return A
				},
				data: function () {
					return u
				},
				dataset: function () {
					return h
				},
				detach: function () {
					return ue
				},
				each: function () {
					return G
				},
				empty: function () {
					return he
				},
				eq: function () {
					return W
				},
				filter: function () {
					return V
				},
				find: function () {
					return le
				},
				focus: function () {
					return Te
				},
				focusin: function () {
					return Ee
				},
				focusout: function () {
					return Se
				},
				forEach: function () {
					return B
				},
				hasClass: function () {
					return n
				},
				height: function () {
					return k
				},
				hide: function () {
					return L
				},
				html: function () {
					return F
				},
				index: function () {
					return j
				},
				indexOf: function () {
					return R
				},
				insertAfter: function () {
					return J
				},
				insertBefore: function () {
					return Q
				},
				is: function () {
					return Y
				},
				keydown: function () {
					return Me
				},
				keypress: function () {
					return Pe
				},
				keyup: function () {
					return Ce
				},
				map: function () {
					return q
				},
				mousedown: function () {
					return ze
				},
				mouseenter: function () {
					return Oe
				},
				mouseleave: function () {
					return Ae
				},
				mousemove: function () {
					return Le
				},
				mouseout: function () {
					return De
				},
				mouseover: function () {
					return Ne
				},
				mouseup: function () {
					return Ie
				},
				next: function () {
					return ee
				},
				nextAll: function () {
					return te
				},
				off: function () {
					return x
				},
				offset: function () {
					return z
				},
				on: function () {
					return b
				},
				once: function () {
					return T
				},
				outerHeight: function () {
					return $
				},
				outerWidth: function () {
					return P
				},
				parent: function () {
					return se
				},
				parents: function () {
					return re
				},
				prepend: function () {
					return K
				},
				prependTo: function () {
					return Z
				},
				prev: function () {
					return ae
				},
				prevAll: function () {
					return ie
				},
				prop: function () {
					return c
				},
				remove: function () {
					return ce
				},
				removeAttr: function () {
					return d
				},
				removeClass: function () {
					return i
				},
				removeData: function () {
					return p
				},
				resize: function () {
					return Ve
				},
				scroll: function () {
					return qe
				},
				scrollLeft: function () {
					return ve
				},
				scrollTo: function () {
					return fe
				},
				scrollTop: function () {
					return me
				},
				show: function () {
					return I
				},
				siblings: function () {
					return ne
				},
				stop: function () {
					return we
				},
				styles: function () {
					return O
				},
				submit: function () {
					return ke
				},
				text: function () {
					return X
				},
				toArray: function () {
					return H
				},
				toggleClass: function () {
					return r
				},
				touchend: function () {
					return Ge
				},
				touchmove: function () {
					return Be
				},
				touchstart: function () {
					return He
				},
				transform: function () {
					return g
				},
				transition: function () {
					return y
				},
				transitionEnd: function () {
					return S
				},
				trigger: function () {
					return E
				},
				val: function () {
					return f
				},
				width: function () {
					return M
				}
			});
			var v = t(3),
				l = _createClass(function e(t) {
					_classCallCheck(this, e);
					for (var a = 0; a < t.length; a += 1) this[a] = t[a];
					return this.length = t.length, this
				});

			function m(e, t) {
				var a = [],
					i = 0;
				if (e && !t && e instanceof l) return e;
				if (e)
					if ("string" == typeof e) {
						var n, s, r = e.trim();
						if (0 <= r.indexOf("<") && 0 <= r.indexOf(">")) {
							var o = "div";
							for (0 === r.indexOf("<li") && (o = "ul"), 0 === r.indexOf("<tr") && (o = "tbody"), 0 !== r.indexOf("<td") && 0 !== r.indexOf("<th") || (o = "tr"), 0 === r.indexOf("<tbody") && (o = "table"), 0 === r.indexOf("<option") && (o = "select"), (s = v.document.createElement(o)).innerHTML = r, i = 0; i < s.childNodes.length; i += 1) a.push(s.childNodes[i])
						} else
							for (n = t || "#" !== e[0] || e.match(/[ .<>:~]/) ? (t || v.document).querySelectorAll(e.trim()) : [v.document.getElementById(e.trim().split("#")[1])], i = 0; i < n.length; i += 1) n[i] && a.push(n[i])
					} else if (e.nodeType || e === v.window || e === v.document) a.push(e);
				else if (0 < e.length && e[0].nodeType)
					for (i = 0; i < e.length; i += 1) a.push(e[i]);
				return new l(a)
			}

			function s(e) {
				for (var t = [], a = 0; a < e.length; a += 1) - 1 === t.indexOf(e[a]) && t.push(e[a]);
				return t
			}

			function w(e) {
				return v.window.requestAnimationFrame ? v.window.requestAnimationFrame(e) : v.window.webkitRequestAnimationFrame ? v.window.webkitRequestAnimationFrame(e) : v.window.setTimeout(e, 1e3 / 60)
			}

			function a(e) {
				if (void 0 !== e)
					for (var t = e.split(" "), a = 0; a < t.length; a += 1)
						for (var i = 0; i < this.length; i += 1) void 0 !== this[i] && void 0 !== this[i].classList && this[i].classList.add(t[a]);
				return this
			}

			function i(e) {
				for (var t = e.split(" "), a = 0; a < t.length; a += 1)
					for (var i = 0; i < this.length; i += 1) void 0 !== this[i] && void 0 !== this[i].classList && this[i].classList.remove(t[a]);
				return this
			}

			function n(e) {
				return !!this[0] && this[0].classList.contains(e)
			}

			function r(e) {
				for (var t = e.split(" "), a = 0; a < t.length; a += 1)
					for (var i = 0; i < this.length; i += 1) void 0 !== this[i] && void 0 !== this[i].classList && this[i].classList.toggle(t[a]);
				return this
			}

			function o(e, t) {
				if (1 === arguments.length && "string" == typeof e) return this[0] ? this[0].getAttribute(e) : void 0;
				for (var a = 0; a < this.length; a += 1)
					if (2 === arguments.length) this[a].setAttribute(e, t);
					else
						for (var i in e) this[a][i] = e[i], this[a].setAttribute(i, e[i]);
				return this
			}

			function d(e) {
				for (var t = 0; t < this.length; t += 1) this[t].removeAttribute(e);
				return this
			}

			function c(e, t) {
				if (1 !== arguments.length || "string" != typeof e) {
					for (var a = 0; a < this.length; a += 1)
						if (2 === arguments.length) this[a][e] = t;
						else
							for (var i in e) this[a][i] = e[i];
					return this
				}
				if (this[0]) return this[0][e]
			}

			function u(e, t) {
				var a;
				if (void 0 === t) return (a = this[0]) ? a.dom7ElementDataStorage && e in a.dom7ElementDataStorage ? a.dom7ElementDataStorage[e] : a.getAttribute("data-".concat(e)) || void 0 : void 0;
				for (var i = 0; i < this.length; i += 1)(a = this[i]).dom7ElementDataStorage || (a.dom7ElementDataStorage = {}), a.dom7ElementDataStorage[e] = t;
				return this
			}

			function p(e) {
				for (var t = 0; t < this.length; t += 1) {
					var a = this[t];
					a.dom7ElementDataStorage && a.dom7ElementDataStorage[e] && (a.dom7ElementDataStorage[e] = null, delete a.dom7ElementDataStorage[e])
				}
			}

			function h() {
				var e = this[0];
				if (e) {
					var t, a = {};
					if (e.dataset)
						for (var i in e.dataset) a[i] = e.dataset[i];
					else
						for (var n = 0; n < e.attributes.length; n += 1) {
							var s = e.attributes[n];
							0 <= s.name.indexOf("data-") && (a[s.name.split("data-")[1].toLowerCase().replace(/-(.)/g, function (e, t) {
								return t.toUpperCase()
							})] = s.value)
						}
					for (t in a) "false" === a[t] ? a[t] = !1 : "true" === a[t] ? a[t] = !0 : parseFloat(a[t]) === +a[t] && (a[t] *= 1);
					return a
				}
			}

			function f(e) {
				var t = this;
				if (void 0 !== e) {
					for (var a = 0; a < t.length; a += 1) {
						var i = t[a];
						if (Array.isArray(e) && i.multiple && "select" === i.nodeName.toLowerCase())
							for (var n = 0; n < i.options.length; n += 1) i.options[n].selected = 0 <= e.indexOf(i.options[n].value);
						else i.value = e
					}
					return t
				}
				if (t[0]) {
					if (t[0].multiple && "select" === t[0].nodeName.toLowerCase()) {
						for (var s = [], r = 0; r < t[0].selectedOptions.length; r += 1) s.push(t[0].selectedOptions[r].value);
						return s
					}
					return t[0].value
				}
			}

			function g(e) {
				for (var t = 0; t < this.length; t += 1) {
					var a = this[t].style;
					a.webkitTransform = e, a.transform = e
				}
				return this
			}

			function y(e) {
				"string" != typeof e && (e = "".concat(e, "ms"));
				for (var t = 0; t < this.length; t += 1) {
					var a = this[t].style;
					a.webkitTransitionDuration = e, a.transitionDuration = e
				}
				return this
			}

			function b() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				var i = t[0],
					s = t[1],
					r = t[2],
					n = t[3];

				function o(e) {
					var t = e.target;
					if (t) {
						var a = e.target.dom7EventData || [];
						if (a.indexOf(e) < 0 && a.unshift(e), m(t).is(s)) r.apply(t, a);
						else
							for (var i = m(t).parents(), n = 0; n < i.length; n += 1) m(i[n]).is(s) && r.apply(i[n], a)
					}
				}

				function l(e) {
					var t = e && e.target && e.target.dom7EventData || [];
					t.indexOf(e) < 0 && t.unshift(e), r.apply(this, t)
				}
				"function" == typeof t[1] && (i = t[0], r = t[1], n = t[2], s = void 0);
				for (var d, n = n || !1, c = i.split(" "), u = 0; u < this.length; u += 1) {
					var p = this[u];
					if (s)
						for (d = 0; d < c.length; d += 1) {
							var h = c[d];
							p.dom7LiveListeners || (p.dom7LiveListeners = {}), p.dom7LiveListeners[h] || (p.dom7LiveListeners[h] = []), p.dom7LiveListeners[h].push({
								listener: r,
								proxyListener: o
							}), p.addEventListener(h, o, n)
						} else
							for (d = 0; d < c.length; d += 1) {
								var f = c[d];
								p.dom7Listeners || (p.dom7Listeners = {}), p.dom7Listeners[f] || (p.dom7Listeners[f] = []), p.dom7Listeners[f].push({
									listener: r,
									proxyListener: l
								}), p.addEventListener(f, l, n)
							}
				}
				return this
			}

			function x() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				for (var i = t[0], n = t[1], s = t[2], r = t[3], o = ("function" == typeof t[1] && (i = t[0], s = t[1], r = t[2], n = void 0), r = r || !1, i.split(" ")), l = 0; l < o.length; l += 1)
					for (var d = o[l], c = 0; c < this.length; c += 1) {
						var u = this[c],
							p = void 0;
						if (!n && u.dom7Listeners ? p = u.dom7Listeners[d] : n && u.dom7LiveListeners && (p = u.dom7LiveListeners[d]), p && p.length)
							for (var h = p.length - 1; 0 <= h; --h) {
								var f = p[h];
								(s && f.listener === s || s && f.listener && f.listener.dom7proxy && f.listener.dom7proxy === s || !s) && (u.removeEventListener(d, f.proxyListener, r), p.splice(h, 1))
							}
					}
				return this
			}

			function T() {
				for (var i = this, e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				var n = t[0],
					s = t[1],
					r = t[2],
					o = t[3];

				function l() {
					for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
					r.apply(this, t), i.off(n, s, l, o), l.dom7proxy && delete l.dom7proxy
				}
				return "function" == typeof t[1] && (n = t[0], r = t[1], o = t[2], s = void 0), l.dom7proxy = r, i.on(n, s, l, o)
			}

			function E() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				for (var i = t[0].split(" "), n = t[1], s = 0; s < i.length; s += 1)
					for (var r = i[s], o = 0; o < this.length; o += 1) {
						var l = this[o],
							d = void 0;
						try {
							d = new v.window.CustomEvent(r, {
								detail: n,
								bubbles: !0,
								cancelable: !0
							})
						} catch (e) {
							(d = v.document.createEvent("Event")).initEvent(r, !0, !0), d.detail = n
						}
						l.dom7EventData = t.filter(function (e, t) {
							return 0 < t
						}), l.dispatchEvent(d), l.dom7EventData = [], delete l.dom7EventData
					}
				return this
			}

			function S(t) {
				var a, i = ["webkitTransitionEnd", "transitionend"],
					n = this;

				function s(e) {
					if (e.target === this)
						for (t.call(this, e), a = 0; a < i.length; a += 1) n.off(i[a], s)
				}
				if (t)
					for (a = 0; a < i.length; a += 1) n.on(i[a], s);
				return this
			}

			function C(t) {
				var a, i = ["webkitAnimationEnd", "animationend"],
					n = this;

				function s(e) {
					if (e.target === this)
						for (t.call(this, e), a = 0; a < i.length; a += 1) n.off(i[a], s)
				}
				if (t)
					for (a = 0; a < i.length; a += 1) n.on(i[a], s);
				return this
			}

			function M() {
				return this[0] === v.window ? v.window.innerWidth : 0 < this.length ? parseFloat(this.css("width")) : null
			}

			function P(e) {
				return 0 < this.length ? e ? (e = this.styles(), this[0].offsetWidth + parseFloat(e.getPropertyValue("margin-right")) + parseFloat(e.getPropertyValue("margin-left"))) : this[0].offsetWidth : null
			}

			function k() {
				return this[0] === v.window ? v.window.innerHeight : 0 < this.length ? parseFloat(this.css("height")) : null
			}

			function $(e) {
				return 0 < this.length ? e ? (e = this.styles(), this[0].offsetHeight + parseFloat(e.getPropertyValue("margin-top")) + parseFloat(e.getPropertyValue("margin-bottom"))) : this[0].offsetHeight : null
			}

			function z() {
				var e, t, a, i, n;
				return 0 < this.length ? (e = (n = this[0]).getBoundingClientRect(), a = v.document.body, t = n.clientTop || a.clientTop || 0, a = n.clientLeft || a.clientLeft || 0, i = n === v.window ? v.window.scrollY : n.scrollTop, n = n === v.window ? v.window.scrollX : n.scrollLeft, {
					top: e.top + i - t,
					left: e.left + n - a
				}) : null
			}

			function L() {
				for (var e = 0; e < this.length; e += 1) this[e].style.display = "none";
				return this
			}

			function I() {
				for (var e = 0; e < this.length; e += 1) {
					var t = this[e];
					"none" === t.style.display && (t.style.display = ""), "none" === v.window.getComputedStyle(t, null).getPropertyValue("display") && (t.style.display = "block")
				}
				return this
			}

			function O() {
				return this[0] ? v.window.getComputedStyle(this[0], null) : {}
			}

			function A(e, t) {
				var a;
				if (1 === arguments.length) {
					if ("string" != typeof e) {
						for (a = 0; a < this.length; a += 1)
							for (var i in e) this[a].style[i] = e[i];
						return this
					}
					if (this[0]) return v.window.getComputedStyle(this[0], null).getPropertyValue(e)
				}
				if (2 === arguments.length && "string" == typeof e)
					for (a = 0; a < this.length; a += 1) this[a].style[e] = t;
				return this
			}

			function H() {
				for (var e = [], t = 0; t < this.length; t += 1) e.push(this[t]);
				return e
			}

			function G(e) {
				if (e)
					for (var t = 0; t < this.length; t += 1)
						if (!1 === e.call(this[t], t, this[t])) return this;
				return this
			}

			function B(e) {
				if (e)
					for (var t = 0; t < this.length; t += 1)
						if (!1 === e.call(this[t], this[t], t)) return this;
				return this
			}

			function V(e) {
				for (var t = [], a = 0; a < this.length; a += 1) e.call(this[a], a, this[a]) && t.push(this[a]);
				return new l(t)
			}

			function q(e) {
				for (var t = [], a = 0; a < this.length; a += 1) t.push(e.call(this[a], a, this[a]));
				return new l(t)
			}

			function F(e) {
				if (void 0 === e) return this[0] ? this[0].innerHTML : void 0;
				for (var t = 0; t < this.length; t += 1) this[t].innerHTML = e;
				return this
			}

			function X(e) {
				if (void 0 === e) return this[0] ? this[0].textContent.trim() : null;
				for (var t = 0; t < this.length; t += 1) this[t].textContent = e;
				return this
			}

			function Y(e) {
				var t, a, i = this[0];
				if (i && void 0 !== e)
					if ("string" == typeof e) {
						if (i.matches) return i.matches(e);
						if (i.webkitMatchesSelector) return i.webkitMatchesSelector(e);
						if (i.msMatchesSelector) return i.msMatchesSelector(e);
						for (t = m(e), a = 0; a < t.length; a += 1)
							if (t[a] === i) return !0
					} else {
						if (e === v.document) return i === v.document;
						if (e === v.window) return i === v.window;
						if (e.nodeType || e instanceof l)
							for (t = e.nodeType ? [e] : e, a = 0; a < t.length; a += 1)
								if (t[a] === i) return !0
					} return !1
			}

			function R(e) {
				for (var t = 0; t < this.length; t += 1)
					if (this[t] === e) return t;
				return -1
			}

			function j() {
				var e, t = this[0];
				if (t) {
					for (e = 0; null !== (t = t.previousSibling);) 1 === t.nodeType && (e += 1);
					return e
				}
			}

			function W(e) {
				var t;
				return void 0 === e ? this : (t = this.length) - 1 < e ? new l([]) : e < 0 ? (t = t + e) < 0 ? new l([]) : new l([this[t]]) : new l([this[e]])
			}

			function _() {
				for (var e = 0; e < arguments.length; e += 1)
					for (var t = e < 0 || arguments.length <= e ? void 0 : arguments[e], a = 0; a < this.length; a += 1)
						if ("string" == typeof t) {
							var i = v.document.createElement("div");
							for (i.innerHTML = t; i.firstChild;) this[a].appendChild(i.firstChild)
						} else if (t instanceof l)
					for (var n = 0; n < t.length; n += 1) this[a].appendChild(t[n]);
				else this[a].appendChild(t);
				return this
			}

			function U(e) {
				return m(e).append(this), this
			}

			function K(e) {
				for (var t, a = 0; a < this.length; a += 1)
					if ("string" == typeof e) {
						var i = v.document.createElement("div");
						for (i.innerHTML = e, t = i.childNodes.length - 1; 0 <= t; --t) this[a].insertBefore(i.childNodes[t], this[a].childNodes[0])
					} else if (e instanceof l)
					for (t = 0; t < e.length; t += 1) this[a].insertBefore(e[t], this[a].childNodes[0]);
				else this[a].insertBefore(e, this[a].childNodes[0]);
				return this
			}

			function Z(e) {
				return m(e).prepend(this), this
			}

			function Q(e) {
				for (var t = m(e), a = 0; a < this.length; a += 1)
					if (1 === t.length) t[0].parentNode.insertBefore(this[a], t[0]);
					else if (1 < t.length)
					for (var i = 0; i < t.length; i += 1) t[i].parentNode.insertBefore(this[a].cloneNode(!0), t[i])
			}

			function J(e) {
				for (var t = m(e), a = 0; a < this.length; a += 1)
					if (1 === t.length) t[0].parentNode.insertBefore(this[a], t[0].nextSibling);
					else if (1 < t.length)
					for (var i = 0; i < t.length; i += 1) t[i].parentNode.insertBefore(this[a].cloneNode(!0), t[i].nextSibling)
			}

			function ee(e) {
				return 0 < this.length ? e ? this[0].nextElementSibling && m(this[0].nextElementSibling).is(e) ? new l([this[0].nextElementSibling]) : new l([]) : this[0].nextElementSibling ? new l([this[0].nextElementSibling]) : new l([]) : new l([])
			}

			function te(e) {
				var t = [],
					a = this[0];
				if (!a) return new l([]);
				for (; a.nextElementSibling;) {
					var i = a.nextElementSibling;
					(!e || m(i).is(e)) && t.push(i), a = i
				}
				return new l(t)
			}

			function ae(e) {
				var t;
				return 0 < this.length ? (t = this[0], e ? t.previousElementSibling && m(t.previousElementSibling).is(e) ? new l([t.previousElementSibling]) : new l([]) : t.previousElementSibling ? new l([t.previousElementSibling]) : new l([])) : new l([])
			}

			function ie(e) {
				var t = [],
					a = this[0];
				if (!a) return new l([]);
				for (; a.previousElementSibling;) {
					var i = a.previousElementSibling;
					(!e || m(i).is(e)) && t.push(i), a = i
				}
				return new l(t)
			}

			function ne(e) {
				return this.nextAll(e).add(this.prevAll(e))
			}

			function se(e) {
				for (var t = [], a = 0; a < this.length; a += 1) null === this[a].parentNode || e && !m(this[a].parentNode).is(e) || t.push(this[a].parentNode);
				return m(s(t))
			}

			function re(e) {
				for (var t = [], a = 0; a < this.length; a += 1)
					for (var i = this[a].parentNode; i;) e && !m(i).is(e) || t.push(i), i = i.parentNode;
				return m(s(t))
			}

			function oe(e) {
				var t = this;
				return void 0 === e ? new l([]) : t.is(e) ? t : t.parents(e).eq(0)
			}

			function le(e) {
				for (var t = [], a = 0; a < this.length; a += 1)
					for (var i = this[a].querySelectorAll(e), n = 0; n < i.length; n += 1) t.push(i[n]);
				return new l(t)
			}

			function de(e) {
				for (var t = [], a = 0; a < this.length; a += 1)
					for (var i = this[a].childNodes, n = 0; n < i.length; n += 1) e ? 1 === i[n].nodeType && m(i[n]).is(e) && t.push(i[n]) : 1 === i[n].nodeType && t.push(i[n]);
				return new l(s(t))
			}

			function ce() {
				for (var e = 0; e < this.length; e += 1) this[e].parentNode && this[e].parentNode.removeChild(this[e]);
				return this
			}

			function ue() {
				return this.remove()
			}

			function pe() {
				for (var e, t = arguments.length, a = new Array(t), i = 0; i < t; i++) a[i] = arguments[i];
				for (e = 0; e < a.length; e += 1)
					for (var n = m(a[e]), s = 0; s < n.length; s += 1) this[this.length] = n[s], this.length += 1;
				return this
			}

			function he() {
				for (var e = 0; e < this.length; e += 1) {
					var t = this[e];
					if (1 === t.nodeType) {
						for (var a = 0; a < t.childNodes.length; a += 1) t.childNodes[a].parentNode && t.childNodes[a].parentNode.removeChild(t.childNodes[a]);
						t.textContent = ""
					}
				}
				return this
			}

			function fe() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				var h = t[0],
					f = t[1],
					m = t[2],
					v = t[3],
					g = t[4];
				return 4 === t.length && "function" == typeof v && (g = v, h = t[0], f = t[1], m = t[2], g = t[3], v = t[4]), void 0 === v && (v = "swing"), this.each(function () {
					var i, n, e, s, r, o, l, d, c = this,
						u = 0 < f || 0 === f,
						p = 0 < h || 0 === h;
					void 0 === v && (v = "swing"), u && (i = c.scrollTop, m || (c.scrollTop = f)), p && (n = c.scrollLeft, m || (c.scrollLeft = h)), m && (u && (e = c.scrollHeight - c.offsetHeight, s = Math.max(Math.min(f, e), 0)), p && (e = c.scrollWidth - c.offsetWidth, r = Math.max(Math.min(h, e), 0)), d = null, u && s === i && (u = !1), p && r === n && (p = !1), w(function e() {
						var t, a = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : (new Date).getTime(),
							a = (null === d && (d = a), Math.max(Math.min((a - d) / m, 1), 0)),
							a = "linear" === v ? a : .5 - Math.cos(a * Math.PI) / 2;
						u && (o = i + a * (s - i)), p && (l = n + a * (r - n)), u && i < s && s <= o && (c.scrollTop = s, t = !0), u && s < i && o <= s && (c.scrollTop = s, t = !0), p && n < r && r <= l && (c.scrollLeft = r, t = !0), p && r < n && l <= r && (c.scrollLeft = r, t = !0), t ? g && g() : (u && (c.scrollTop = o), p && (c.scrollLeft = l), w(e))
					}))
				})
			}

			function me() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				var i = t[0],
					n = t[1],
					s = t[2],
					r = t[3];
				3 === t.length && "function" == typeof s && (i = t[0], n = t[1], r = t[2], s = t[3]);
				return void 0 === i ? 0 < this.length ? this[0].scrollTop : null : this.scrollTo(void 0, i, n, s, r)
			}

			function ve() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				var i = t[0],
					n = t[1],
					s = t[2],
					r = t[3];
				3 === t.length && "function" == typeof s && (i = t[0], n = t[1], r = t[2], s = t[3]);
				return void 0 === i ? 0 < this.length ? this[0].scrollLeft : null : this.scrollTo(i, void 0, n, s, r)
			}

			function ge(e, t) {
				var a = this,
					m = {
						props: Object.assign({}, e),
						params: Object.assign({
							duration: 300,
							easing: "swing"
						}, t),
						elements: a,
						animating: !1,
						que: [],
						easingProgress: function (e, t) {
							return "swing" === e ? .5 - Math.cos(t * Math.PI) / 2 : "function" == typeof e ? e(t) : t
						},
						stop: function () {
							var e;
							m.frameId && (e = m.frameId, v.window.cancelAnimationFrame ? v.window.cancelAnimationFrame(e) : v.window.webkitCancelAnimationFrame ? v.window.webkitCancelAnimationFrame(e) : v.window.clearTimeout(e)), m.animating = !1, m.elements.each(function (e, t) {
								delete t.dom7AnimateInstance
							}), m.que = []
						},
						done: function (e) {
							m.animating = !1, m.elements.each(function (e, t) {
								delete t.dom7AnimateInstance
							}), e && e(a), 0 < m.que.length && (e = m.que.shift(), m.animate(e[0], e[1]))
						},
						animate: function (l, o) {
							var d, c, u, p, h, f, t;
							return m.animating ? m.que.push([l, o]) : (d = [], c = (m.elements.each(function (t, a) {
								var i, n, s, r, o;
								a.dom7AnimateInstance || (m.elements[t].dom7AnimateInstance = m), d[t] = {
									container: a
								}, Object.keys(l).forEach(function (e) {
									i = v.window.getComputedStyle(a, null).getPropertyValue(e).replace(",", "."), n = parseFloat(i), s = i.replace(n, ""), r = parseFloat(l[e]), o = l[e] + s, d[t][e] = {
										initialFullValue: i,
										initialValue: n,
										unit: s,
										finalValue: r,
										finalFullValue: o,
										currentValue: n
									}
								})
							}), null), h = p = 0, t = !1, m.animating = !0, m.frameId = w(function e() {
								var r;
								u = (new Date).getTime(), t || (t = !0, o.begin && o.begin(a)), null === c && (c = u), o.progress && o.progress(a, Math.max(Math.min((u - c) / o.duration, 1), 0), c + o.duration - u < 0 ? 0 : c + o.duration - u, c), d.forEach(function (e) {
									var s = e;
									f || s.done || Object.keys(l).forEach(function (e) {
										var t, a, i, n;
										f || s.done || (r = Math.max(Math.min((u - c) / o.duration, 1), 0), r = m.easingProgress(o.easing, r), t = (i = s[e]).initialValue, a = i.finalValue, i = i.unit, s[e].currentValue = t + r * (a - t), n = s[e].currentValue, (t < a && a <= n || a < t && n <= a) && (s.container.style[e] = a + i, (h += 1) === Object.keys(l).length && (s.done = !0, p += 1), p === d.length && (f = !0)), f ? m.done(o.complete) : s.container.style[e] = n + i)
									})
								}), f || (m.frameId = w(e))
							})), m
						}
					};
				if (0 !== m.elements.length) {
					for (var i, n = 0; n < m.elements.length; n += 1) m.elements[n].dom7AnimateInstance ? i = m.elements[n].dom7AnimateInstance : m.elements[n].dom7AnimateInstance = m;
					i = i || m, "stop" === e ? i.stop() : i.animate(m.props, m.params)
				}
				return a
			}

			function we() {
				for (var e = 0; e < this.length; e += 1) this[e].dom7AnimateInstance && this[e].dom7AnimateInstance.stop()
			}
			m.fn = l.prototype, m.Class = l, m.Dom7 = l;
			var ye = "resize scroll".split(" ");

			function D(e) {
				for (var t = arguments.length, a = new Array(1 < t ? t - 1 : 0), i = 1; i < t; i++) a[i - 1] = arguments[i];
				if (void 0 !== a[0]) return this.on.apply(this, [e].concat(a));
				for (var n = 0; n < this.length; n += 1) ye.indexOf(e) < 0 && (e in this[n] ? this[n][e]() : m(this[n]).trigger(e));
				return this
			}

			function be() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["click"].concat(t))
			}

			function xe() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["blur"].concat(t))
			}

			function Te() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["focus"].concat(t))
			}

			function Ee() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["focusin"].concat(t))
			}

			function Se() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["focusout"].concat(t))
			}

			function Ce() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["keyup"].concat(t))
			}

			function Me() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["keydown"].concat(t))
			}

			function Pe() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["keypress"].concat(t))
			}

			function ke() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["submit"].concat(t))
			}

			function $e() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["change"].concat(t))
			}

			function ze() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["mousedown"].concat(t))
			}

			function Le() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["mousemove"].concat(t))
			}

			function Ie() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["mouseup"].concat(t))
			}

			function Oe() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["mouseenter"].concat(t))
			}

			function Ae() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["mouseleave"].concat(t))
			}

			function De() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["mouseout"].concat(t))
			}

			function Ne() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["mouseover"].concat(t))
			}

			function He() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["touchstart"].concat(t))
			}

			function Ge() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["touchend"].concat(t))
			}

			function Be() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["touchmove"].concat(t))
			}

			function Ve() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["resize"].concat(t))
			}

			function qe() {
				for (var e = arguments.length, t = new Array(e), a = 0; a < e; a++) t[a] = arguments[a];
				return D.bind(this).apply(void 0, ["scroll"].concat(t))
			}
		}, function (e, t, a) {
			function i(e) {
				return null !== e && "object" === _typeof(e) && "constructor" in e && e.constructor === Object
			}

			function n(t, a) {
				void 0 === t && (t = {}), void 0 === a && (a = {}), Object.keys(a).forEach(function (e) {
					void 0 === t[e] ? t[e] = a[e] : i(a[e]) && i(t[e]) && 0 < Object.keys(a[e]).length && n(t[e], a[e])
				})
			}
			a.r(t), a.d(t, {
				document: function () {
					return s
				},
				extend: function () {
					return n
				},
				window: function () {
					return r
				}
			});
			var s = "undefined" != typeof document ? document : {},
				a = {
					body: {},
					addEventListener: function () {},
					removeEventListener: function () {},
					activeElement: {
						blur: function () {},
						nodeName: ""
					},
					querySelector: function () {
						return null
					},
					querySelectorAll: function () {
						return []
					},
					getElementById: function () {
						return null
					},
					createEvent: function () {
						return {
							initEvent: function () {}
						}
					},
					createElement: function () {
						return {
							children: [],
							childNodes: [],
							style: {},
							setAttribute: function () {},
							getElementsByTagName: function () {
								return []
							}
						}
					},
					createElementNS: function () {
						return {}
					},
					importNode: function () {
						return null
					},
					location: {
						hash: "",
						host: "",
						hostname: "",
						href: "",
						origin: "",
						pathname: "",
						protocol: "",
						search: ""
					}
				},
				r = (n(s, a), "undefined" != typeof window ? window : {});
			n(r, {
				document: a,
				navigator: {
					userAgent: ""
				},
				location: {
					hash: "",
					host: "",
					hostname: "",
					href: "",
					origin: "",
					pathname: "",
					protocol: "",
					search: ""
				},
				history: {
					replaceState: function () {},
					pushState: function () {},
					go: function () {},
					back: function () {}
				},
				CustomEvent: function () {
					return this
				},
				addEventListener: function () {},
				removeEventListener: function () {},
				getComputedStyle: function () {
					return {
						getPropertyValue: function () {
							return ""
						}
					}
				},
				Image: function () {},
				Date: function () {},
				screen: {},
				setTimeout: function () {},
				clearTimeout: function () {},
				matchMedia: function () {
					return {}
				}
			})
		}, function (e, t, a) {
			a.r(t), a.d(t, {
				document: function () {
					return i
				},
				window: function () {
					return n
				}
			});
			var i = "undefined" == typeof document ? {
					body: {},
					addEventListener: function () {},
					removeEventListener: function () {},
					activeElement: {
						blur: function () {},
						nodeName: ""
					},
					querySelector: function () {
						return null
					},
					querySelectorAll: function () {
						return []
					},
					getElementById: function () {
						return null
					},
					createEvent: function () {
						return {
							initEvent: function () {}
						}
					},
					createElement: function () {
						return {
							children: [],
							childNodes: [],
							style: {},
							setAttribute: function () {},
							getElementsByTagName: function () {
								return []
							}
						}
					},
					location: {
						hash: ""
					}
				} : document,
				n = "undefined" == typeof window ? {
					document: i,
					navigator: {
						userAgent: ""
					},
					location: {},
					history: {},
					CustomEvent: function () {
						return this
					},
					addEventListener: function () {},
					removeEventListener: function () {},
					getComputedStyle: function () {
						return {
							getPropertyValue: function () {
								return ""
							}
						}
					},
					Image: function () {},
					Date: function () {},
					screen: {},
					setTimeout: function () {},
					clearTimeout: function () {}
				} : window
		}],
		i = {};

	function n(e) {
		var t = i[e];
		return void 0 !== t || (t = i[e] = {
			exports: {}
		}, a[e](t, t.exports, n)), t.exports
	}

	function s() {
		var i = setInterval(function () {
			var e, t, a = new Date;
			for (e in d) d.hasOwnProperty.call(d, e) && (t = d[e], a.getHours() == parseInt(t.time.split(":")[0]) && a.getMinutes() == parseInt(t.time.split(":")[1] - 1) && (document.querySelector(".screen-will-adzan").classList.remove("hiden"), document.querySelector(".menjelang-adzan").innerHTML = t.name, function (a) {
				var i = new Date,
					n = (i.setHours(d[a].time.split(":")[0]), i.setMinutes(d[a].time.split(":")[1]), i.setSeconds(0), setInterval(function () {
						var e = (new Date).getTime(),
							e = i.getTime() - e,
							t = ("0" + Math.floor(e % 6e4 / 1e3)).slice(-2);
						document.querySelector(".timer-countdown-adzan").innerHTML = t, e < 1 && (clearInterval(n), c(a))
					}, 1e3))
			}(e), clearInterval(i)))
		}, 1e3)
	}

	function r() {
		document.querySelector(".time-iqmomah").className = "time-iqmomah hiden", document.querySelector(".time-silent").className = "time-silent";
		var t = new Date,
			a = (t.setMinutes(t.getMinutes() + e), t.setSeconds(0), setInterval(function () {
				var e = (new Date).getTime();
				t.getTime() - e < 1 && (clearInterval(a), window.location.reload(!0))
			}, 1e3))
	}

	function o() {
		var t = new Date,
			a = (t.setMinutes(t.getMinutes() + l), t.setSeconds(0), setInterval(function () {
				var e = (new Date).getTime();
				t.getTime() - e < 1 && (clearInterval(a), document.querySelector(".screen-will-adzan").classList.contains("hiden") && document.querySelector(".time-silent").classList.contains("hiden") && document.querySelector(".time-iqmomah").classList.contains("hiden") && !document.querySelector(".table-kajian").classList.contains("hiden") && (document.querySelector(".jasma-main_slider").className = "jasma-main_slider", document.querySelector(".table-kajian").className = "table-kajian hiden", window.location.reload(!0)))
			}, 1e3))
	}
	n.d = function (e, t) {
		for (var a in t) n.o(t, a) && !n.o(e, a) && Object.defineProperty(e, a, {
			enumerable: !0,
			get: t[a]
		})
	}, n.o = function (e, t) {
		return Object.prototype.hasOwnProperty.call(e, t)
	}, (n.r = function (e) {
		"undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {
			value: "Module"
		}), Object.defineProperty(e, "__esModule", {
			value: !0
		})
	})({}), new(n(1).default)(".jasma-main_swiper", {
		effect: "coverflow",
		grabCursor: !0,
		centeredSlides: !0,
		slidesPerView: "1",
		initialSlide: 1,
		loop: !0,
		autoplay: {
			delay: 7e3,
			disableOnInteraction: !1
		},
		coverflowEffect: {
			rotate: 0,
			stretch: 0,
			depth: 150,
			modifier: 2,
			slideShadows: !1
		}
	});
	var e = 1,
		l = 1,
		d = [{
			name: "Imsak",
			time: "04:30"
		}, {
			name: "Subuh",
			time: "04:50"
		}, {
			name: "Dzuhur",
			time: "09:03"
		}, {
			name: "Ashar",
			time: "15:00"
		}, {
			name: "Magrib",
			time: "17:07"
		}, {
			name: "Isya",
			time: "19:03"
		}],
		c = (setInterval(function () {
			var e = (new Date).toTimeString().split(" ")[0];
			document.querySelector(".waktu-berjalan-main").innerHTML = e, document.querySelector(".time-silent_time").innerHTML = e
		}, 1e3), function (e) {
			document.querySelector(".timer-countdown-adzan").innerHTML = "0", document.querySelector(".screen-will-adzan").className = "screen-will-adzan hiden", document.querySelector(".time-iqmomah").className = "time-iqmomah", document.querySelector(".name-iqomah").innerHTML = d[e].name, document.querySelector(".jasma-main").className = "jasma-main hiden";
			var i = new Date,
				n = (i.setMinutes(i.getMinutes() + 1), i.setSeconds(0), setInterval(function () {
					var e = (new Date).getTime(),
						e = i.getTime() - e,
						t = ("0" + Math.floor(e % 36e5 / 6e4)).slice(-2),
						a = ("0" + Math.floor(e % 6e4 / 1e3)).slice(-2);
					document.querySelector(".time-iqmomah_time").innerHTML = t + ":" + a, e < 1 && (clearInterval(n), r())
				}, 1e3))
		});
	document.addEventListener("DOMContentLoaded", function () {
		for (var e in d) {
			var t;
			d.hasOwnProperty.call(d, e) && (t = d[e], document.querySelector(".name-pray-" + e).innerHTML = t.name, document.querySelector(".time-pray-" + e).innerHTML = t.time)
		}
		var a, i;
		s(), (a = new Date).setMinutes(a.getMinutes() + l), a.setSeconds(0), i = setInterval(function () {
			var e = (new Date).getTime();
			a.getTime() - e < 1 && (clearInterval(i), document.querySelector(".screen-will-adzan").classList.contains("hiden") && document.querySelector(".time-silent").classList.contains("hiden") && document.querySelector(".time-iqmomah").classList.contains("hiden") && !document.querySelector(".jasma-main_slider").classList.contains("hiden") && (document.querySelector(".jasma-main_slider").className = "jasma-main_slider hiden", document.querySelector(".table-kajian").className = "table-kajian", o()))
		}, 1e3)
	}, !1)
}();