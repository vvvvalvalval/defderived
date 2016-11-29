(ns defderived.core
  (:require [defderived.impl :as impl])
  )

(defn derived-fn
  "Given:
  * `get-deps`, a 0-arity function which returns a (n-length) vector of dependencies
  * `f`, an n-arity function which computes the derived value from the dependencies,

  Returns a 0-arity function which returns the derived value,
  and uses caching to re-compute it only if the dependencies change."
  [get-deps f]
  (let [cache (atom nil)]
    (fn []
      (let [^defderived.impl.DepsAndV cached @cache
            deps (get-deps)
            stale (or
                    (nil? cached)
                    (not= (.deps cached) deps))]
        (if stale
          (let [v (apply f deps)]
            (reset! cache (impl/->DepsAndV deps v))
            v)
          (.v cached))
        ))))

#?(:clj
   (defmacro derived
     "Given:
  * a `bindings` vector, mapping local names (unqualified symbols) to expressions
  * an `expr` expression, which computes the value
  derived from the dependencies in `bindings`,

  Returns a 0-arity function which will return the value computed in `expr`.

  Uses caching to avoid recomputing the value,
  so as long as the dependincies expressed in `bindings` do not change,
  the derived value will be returned immediately instead of being recomputed."
     [bindings expr]
     (let [deps-args (into [] (take-nth 2) bindings)
           deps-vector-expr (into [] (comp (drop 1) (take-nth 2)) bindings)]
       `(derived-fn
          (fn [] ~deps-vector-expr)
          (fn ~deps-args ~expr))))
   )

#?(:clj
   (defmacro defderived
     "Shorthand for (def <name> (derived ...))."
     [symbol bindings expr]
     `(def ~symbol (derived ~bindings ~expr)))
   )
