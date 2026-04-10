(ns metasimple.concrete.rules.rules-data-test
  (:require
    [metasimple.concrete.rules]
    [metasimple.concrete.rules.compiler :as com]
    [metasimple.concrete.rules.testfacts]))


(def the-rules
  [{:doc  "Rule to determine whether it is indeed cold and windy."
    :name "metasimple.concrete.rules.rules-data-test/is-cold-and-windy-data"
    :lhs  [{:type        (if (com/compiling-cljs?) 'metasimple.concrete.rules.testfacts/Temperature 'metasimple.concrete.rules.testfacts.Temperature)
            :constraints '[(< temperature 20)
                           (== ?t temperature)]}
           {:type        (if (com/compiling-cljs?) 'metasimple.concrete.rules.testfacts/WindSpeed 'metasimple.concrete.rules.testfacts.WindSpeed)
            :constraints '[(> windspeed 30)
                           (== ?w windspeed)]}]
    :rhs  '(metasimple.concrete.rules/insert! (metasimple.concrete.rules.testfacts/->ColdAndWindy ?t ?w))}

   {:name   "metasimple.concrete.rules.rules-data-test/find-cold-and-windy-data"
    :lhs    [{:fact-binding :?fact
              :type         (if (com/compiling-cljs?) 'metasimple.concrete.rules.testfacts/ColdAndWindy 'metasimple.concrete.rules.testfacts.ColdAndWindy)
              :constraints  []}]
    :params #{}}])


(defn weather-rules
  "Return some weather rules"
  []
  the-rules)


(def the-rules-with-keyword-names (mapv #(update % :name keyword) the-rules))


(defn weather-rules-with-keyword-names
  "Return some weather rules using keyword names"
  []
  the-rules-with-keyword-names)
