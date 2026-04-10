(ns metasimple.concrete.sample-ruleset
  (:refer-clojure :exclude [==])
  (:require
    [clojure.pprint :refer :all]
    [metasimple.concrete.rules :refer :all]
    [metasimple.concrete.rules.dsl :refer :all]
    [metasimple.concrete.rules.testfacts :refer :all])
  (:import
    (metasimple.concrete.rules.testfacts
      Cold
      ColdAndWindy
      LousyWeather
      Temperature
      WindSpeed)))


;; These rules are used for unit testing loading from a namespace.
(defquery freezing-locations
  "Query the freezing locations."
  []
  (Temperature (< temperature 32) (== ?loc location)))

(defrule is-cold-and-windy
  "Rule to determine whether it is indeed cold and windy."

  (Temperature (< temperature 20) (== ?t temperature))
  (WindSpeed (> windspeed 30) (== ?w windspeed))
  =>
  (insert! (->ColdAndWindy ?t ?w)))

(defquery find-cold-and-windy
  []
  (?fact <- ColdAndWindy))

(defquery find-lousy-weather
  []
  (?fact <- LousyWeather))

(comment
  freezing-locations

  ;; Example usage:
  (-> (mk-session 'metasimple.concrete.sample-ruleset)
      (insert (->Temperature 10 "KC")
              (->WindSpeed 40 "KC"))
      (fire-rules)
      (query find-cold-and-windy)))
