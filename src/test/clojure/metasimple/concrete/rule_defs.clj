(ns metasimple.concrete.rule-defs
  (:require
    [metasimple.concrete.rules :refer [defrule defquery insert!]]
    [metasimple.concrete.rules.accumulators :as acc]
    [metasimple.concrete.rules.testfacts :as tf]
    [metasimple.concrete.tools.testing-utils :as tu])
  (:import
    (metasimple.concrete.rules.testfacts
      ColdAndWindy
      Temperature
      WindSpeed)))

;; Rule definitions used for tests in metasimple.concrete.rules-require-test.

(defrule test-rule
  [?t <- Temperature (< temperature 20)]
  =>
  (reset! tu/side-effect-holder ?t))

(defquery cold-query
  []
  [Temperature (< temperature 20) (== ?t temperature)])

;; Accumulator for getting the lowest temperature.
(def lowest-temp (acc/min :temperature))

(defquery coldest-query
  []
  [?t <- lowest-temp :from [Temperature]])

(defrule is-cold-and-windy
  "Rule to determine whether it is indeed cold and windy."

  (Temperature (< temperature 20) (== ?t temperature))
  (WindSpeed (> windspeed 30) (== ?w windspeed))
  =>
  (insert! (tf/->ColdAndWindy ?t ?w)))

(defquery find-cold-and-windy
  []
  [?fact <- ColdAndWindy])
