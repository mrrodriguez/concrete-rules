(ns metasimple.concrete.order-ruleset
  (:refer-clojure :exclude [==])
  (:require
    [metasimple.concrete.rules :refer :all]
    [metasimple.concrete.rules.testfacts :refer :all])
  (:import
    (metasimple.concrete.rules.testfacts
      Cold
      ColdAndWindy
      LousyWeather
      Temperature
      WindSpeed)))

(def ^:dynamic *rule-order-atom* nil)

(def ^{:dynamic true
       :production-seq true}
  *rule-seq-prior*
  [])

(defrule rule-C
  [Cold (constantly true)]
  =>
  (swap! *rule-order-atom* conj :C))

(defrule rule-D
  [Cold (constantly true)]
  =>
  (swap! *rule-order-atom* conj :D))

(def ^{:dynamic true
       :production-seq true}
  *rule-seq-after*
  [])
