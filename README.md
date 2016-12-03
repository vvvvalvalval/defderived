# defderived

A tiny Clojure library, which provides a REPL-friendly substitute for Vars with dependencies.

[![Clojars Project](https://img.shields.io/clojars/v/vvvvalvalval/defderived.svg)](https://clojars.org/vvvvalvalval/defderived)

## Motivation and usage

Imagine for instance that you have three global Vars `#'x`, `#'y` and `#'z`,
   where `#'z` is derived from `#'x` and `#'y`:
   
```clojure
(def x 42)

(def y 51)

(def z (+ x y))

z
;=> 93 
```

The usual reason for storing the value for `#'z` in a Var
 is that it is somewhat expensive to compute from `x` and `y` 
 (which is obviously not the case in this over-simplistic example),
 so you essentially want to cache the result instead of recomputing it on the fly.
 
The problem with this approach is that it impedes interactive development: 
 if you redefine `#'x` or `#'y` during a REPL session, the value stored in `#'z` may become stale.
 
This library lets you define `#'z` as a 0-arity memoized function instead, with explicit dependencies to 
   `#'x` and `#'y`;
   
```clojure
(def x 42)

(def y 51)

(def z 
  (derived [a x 
            b y]
    (+ a b)))

(z) ;; the value of #'z is now a 0-arity, memoized fn (not the computed value) 
;=> 93 
```

As a convenience, you can use the `defderived` macro to make the definition of `#'z` more concise:
 
```clojure
(defderived z
  [a x
   b y]
  (+ a b)) 
```

## TODO

* optional eager resolution for production environments 

## License

Copyright © 2016 Valentin Waeselynck and contributors.

Distributed under the MIT License.
