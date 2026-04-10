(ns metasimple.concrete.rhs-retract-test
  (:require 
            [metasimple.concrete.rules.compiler :as com]
    [clojure.test :refer [is deftest run-tests testing use-fixtures]]
    [metasimple.concrete.rules :refer [fire-rules
                                       insert
                                       insert-all
                                       insert!
                                       retract
                                       query
                                       retract!]]
    [metasimple.concrete.rules.accumulators]
    [metasimple.concrete.rules.testfacts :refer [->Temperature ->Cold]]
    [metasimple.concrete.tools.testing-utils :refer [def-rules-test] :as tu]
    [schema.test :as st])
  (:import
    (metasimple.concrete.rules.testfacts
      Cold
      Temperature)))

(use-fixtures :once st/validate-schemas tu/opts-fixture)

(def-rules-test test-retract!

  {:rules [not-cold-rule [[[Temperature (> temperature 50)]]
                          (retract! (->Cold 20))]]

   :queries [cold-query [[]
                         [[Cold (= ?t temperature)]]]]

   :sessions [empty-session [not-cold-rule cold-query] {}]}

  (let [session (-> empty-session
                  (insert (->Cold 20))
                  (fire-rules))]

    ;; The session should contain our initial cold reading.
    (is (= #{{:?t 20}}
          (set (query session cold-query))))

    ;; Insert a higher temperature and ensure the cold fact was retracted.
    (is (= #{}
          (set (query (-> session
                        (insert (->Temperature 80 "MCI"))
                        (fire-rules))
                 cold-query))))))
