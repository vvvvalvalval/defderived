(ns defderived.core-test
  (:require [clojure.test :refer :all]
            [defderived.core :refer :all]))

(deftest a-test
  (let [get-x (constantly 42)
        ay (atom 3)
        get-y #(deref ay)
        f (fn [x y] [x y (+ x y)])

        df (derived [x (get-x)
                     y (get-y)]
             (f x y))]
    (testing "Basic usage"
      (is (=
            (df)
            (f (get-x) (get-y)))))
    (testing "Works 2nd time"
      (is (=
            (df)
            (f (get-x) (get-y)))))
    (testing "Cache invalidation"
      (reset! ay 5)
      (is (=
            (df)
            (f (get-x) (get-y))))))
  )
