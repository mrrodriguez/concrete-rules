(ns metasimple.concrete.other-ruleset
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

(defrule is-lousy
  (ColdAndWindy (= temperature 15))
  =>
  (insert! (->LousyWeather)))

;; These rules are used for unit testing loading from a namespace.
(defquery subzero-locations
  "Query the subzero locations."
  []
  (Temperature (< temperature 0) (== ?loc location)))

(defquery temp-by-location
  "Query temperatures by location."
  [:?loc]
  (Temperature (== ?temp temperature)
    (== ?loc location)))
