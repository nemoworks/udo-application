(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[49],{nVCV:function(e,t,n){(function(e){e(n("S0k+"))})((function(e){"use strict";e.defineMode("elm",(function(){function e(e,t,n){return t(n),n(e,t)}var t=/[a-z]/,n=/[A-Z]/,r=/[a-zA-Z0-9_]/,i=/[0-9]/,o=/[0-9A-Fa-f]/,u=/[-&*+.\\/<>=?^|:]/,a=/[(),[\]{}]/,f=/[ \v\f]/;function s(){return function(s,k){if(s.eatWhile(f))return null;var m=s.next();if(a.test(m))return"{"===m&&s.eat("-")?e(s,k,l(1)):"["===m&&s.match("glsl|")?e(s,k,w):"builtin";if("'"===m)return e(s,k,h);if('"'===m)return s.eat('"')?s.eat('"')?e(s,k,c):"string":e(s,k,p);if(n.test(m))return s.eatWhile(r),"variable-2";if(t.test(m)){var x=1===s.pos;return s.eatWhile(r),x?"def":"variable"}if(i.test(m)){if("0"===m){if(s.eat(/[xX]/))return s.eatWhile(o),"number"}else s.eatWhile(i);return s.eat(".")&&s.eatWhile(i),s.eat(/[eE]/)&&(s.eat(/[-+]/),s.eatWhile(i)),"number"}return u.test(m)?"-"===m&&s.eat("-")?(s.skipToEnd(),"comment"):(s.eatWhile(u),"keyword"):"_"===m?"keyword":"error"}}function l(e){return 0==e?s():function(t,n){while(!t.eol()){var r=t.next();if("{"==r&&t.eat("-"))++e;else if("-"==r&&t.eat("}")&&(--e,0===e))return n(s()),"comment"}return n(l(e)),"comment"}}function c(e,t){while(!e.eol()){var n=e.next();if('"'===n&&e.eat('"')&&e.eat('"'))return t(s()),"string"}return"string"}function p(e,t){while(e.skipTo('\\"'))e.next(),e.next();return e.skipTo('"')?(e.next(),t(s()),"string"):(e.skipToEnd(),t(s()),"error")}function h(e,t){while(e.skipTo("\\'"))e.next(),e.next();return e.skipTo("'")?(e.next(),t(s()),"string"):(e.skipToEnd(),t(s()),"error")}function w(e,t){while(!e.eol()){var n=e.next();if("|"===n&&e.eat("]"))return t(s()),"string"}return"string"}var k={case:1,of:1,as:1,if:1,then:1,else:1,let:1,in:1,type:1,alias:1,module:1,where:1,import:1,exposing:1,port:1};return{startState:function(){return{f:s()}},copyState:function(e){return{f:e.f}},token:function(e,t){var n=t.f(e,(function(e){t.f=e})),r=e.current();return k.hasOwnProperty(r)?"keyword":n}}})),e.defineMIME("text/x-elm","elm")}))}}]);