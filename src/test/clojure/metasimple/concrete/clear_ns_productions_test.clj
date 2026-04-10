;; Tests that clear-ns-productions! correction clears all vars marked as productions from the namespace.
(ns metasimple.concrete.clear-ns-productions-test
  (:require
    [clojure.test :refer [is deftest run-tests testing use-fixtures]]
    [metasimple.concrete.rules :refer [fire-rules
                                       insert
                                       insert!
                                       query
                                       defrule
                                       defquery
                                       defsession
                                       clear-ns-productions!]]
    [metasimple.concrete.tools.testing-utils :as tu]))

(use-fixtures :each tu/side-effect-holder-fixture)

(defrule rule-to-be-cleared
  [:a]
  =>
  (reset! tu/side-effect-holder :before-clearing)
  (insert! :before-clearing))

(defquery query-to-be-cleared [] [?f <- :before-clearing])

(def ^:production-seq ns-production-seq-to-be-cleared
  [{:doc  "Before clearing"
    :name "metasimple.concrete.clear-ns-productions-test/production-seq-to-be-cleared"
    :lhs  '[{:type        :a
             :constraints []}]
    :rhs  '(metasimple.concrete.rules/insert! :before-clearing-seq)}])

(defsession uncleared-session 'metasimple.concrete.clear-ns-productions-test :fact-type-fn identity)

(clear-ns-productions!)

(defrule rule-after-clearing
  [:a]
  =>
  (insert! :after-clearing))

(defquery query-before-clearing [] [?f <- :before-clearing])
(defquery query-after-clearing [] [?f <- :after-clearing])
(defquery query-before-clearing-seq [] [?f <- :before-clearing-seq])
(defquery query-after-clearing-seq [] [?f <- :after-clearing-seq])

(def ^:production-seq production-seq-after-clearing
  [{:doc  "After clearing"
    :name "metasimple.concrete.clear-ns-productions-test/production-seq-after-clearing"
    :lhs  '[{:type        :a
             :constraints []}]
    :rhs  '(metasimple.concrete.rules/insert! :after-clearing-seq)}])

(defsession cleared-session 'metasimple.concrete.clear-ns-productions-test :fact-type-fn identity)

;; Then tests validating what productions the respective sessions have.
(deftest cleared?
  (let [uncleared (-> uncleared-session (insert :a) (fire-rules))]
    (is (= :before-clearing @tu/side-effect-holder))
    (reset! tu/side-effect-holder nil))
  (let [cleared (-> cleared-session (insert :a) (fire-rules))]
    (testing "cleared-session should not contain any productions before (clear-ns-productions!)"
      (is (= nil @tu/side-effect-holder))
      (is (empty? (query cleared query-before-clearing)))
      (is (not-empty (query cleared query-after-clearing))))
    (is (empty? (query cleared query-before-clearing-seq)))
    (is (not-empty (query cleared query-after-clearing-seq)))))

(deftest query-cleared?
  (let [uncleared (-> uncleared-session (insert :a) (fire-rules))
        cleared (-> cleared-session (insert :a) (fire-rules))]
    (is (not-empty (query uncleared "metasimple.concrete.clear-ns-productions-test/query-to-be-cleared")))
    (is (thrown-with-msg? IllegalArgumentException #"metasimple.concrete.clear-ns-productions-test/query-to-be-cleared"
          (query cleared "metasimple.concrete.clear-ns-productions-test/query-to-be-cleared")))))
