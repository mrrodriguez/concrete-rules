(ns metasimple.concrete.java-test
  (:require
    [clojure.test :refer :all]
    [metasimple.concrete.other-ruleset :as other]
    [metasimple.concrete.rules.testfacts :refer :all]
    [metasimple.concrete.sample-ruleset :as sample])
  (:import
    (metasimple.concrete.rules
      QueryResult
      RuleLoader
      WorkingMemory)
    (metasimple.concrete.rules.testfacts
      Cold
      ColdAndWindy
      First
      Fourth
      LousyWeather
      Second
      Temperature
      Third
      WindSpeed)))

(defn- java-namespace-args
  "The java API expects an arra of strings containing namespace names, so create that."
  []
  (doto (make-array String 2)
    (aset 0 "metasimple.concrete.sample-ruleset")
    (aset 1 "metasimple.concrete.other-ruleset")))

(deftest simple-rule

  (let [;; Simulate use of a typical Javaland object, the array list. 
        ;; Normally we'd just use the Clojure shorthand, but this is testing Java interop specifically
        facts (doto (java.util.ArrayList.)
                (.add (->Temperature 15 "MCI"))
                (.add (->Temperature 10 "BOS"))
                (.add (->Temperature 50 "SFO"))
                (.add (->Temperature -10 "CHI")))

        ;; Testing Java interop, so session is a metasimple.concrete.rules.WorkingMemory object.
        session (-> (RuleLoader/loadRules (java-namespace-args))
                  (.insert facts)
                  (.fireRules))

        subzero-locs (.query session "metasimple.concrete.other-ruleset/subzero-locations" {})
        freezing-locs (.query session "metasimple.concrete.sample-ruleset/freezing-locations" {})]

    (is (= #{"CHI"}
          (set (map #(.getResult % "?loc") subzero-locs))))

    (is (= #{"CHI" "BOS" "MCI"}
          (set (map #(.getResult % "?loc") freezing-locs))))))

(deftest query-with-args
  (let [session
        (-> (RuleLoader/loadRules (java-namespace-args))
          (.insert [(->Temperature 15 "MCI")
                    (->Temperature 10 "BOS")
                    (->Temperature 50 "SFO")
                    (->Temperature -10 "CHI")])
          (.fireRules))

        ;; Simulate invocation from Java by creating a hashmap of arguments.
        java-args (doto (java.util.HashMap.)
                    (.put "?loc" "CHI"))

        chicago-temp (.query session "metasimple.concrete.other-ruleset/temp-by-location" java-args)]

    (is (= #{-10}
          (set (map #(.getResult % "?temp") chicago-temp))))))
