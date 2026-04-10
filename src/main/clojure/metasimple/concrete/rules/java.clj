(ns metasimple.concrete.rules.java
  "This namespace is for internal use and may move in the future.
  Java support. Users should use the Java API, or the metasimple.concrete.rules namespace from Clojure."
  (:refer-clojure :exclude [==])
  (:require
    [metasimple.concrete.rules :as concrete]
    [metasimple.concrete.rules.compiler :as com]
    [metasimple.concrete.rules.engine :as eng]
    [metasimple.concrete.rules.memory :as mem])
  (:import
    (metasimple.concrete.rules
      QueryResult
      WorkingMemory)))

(deftype JavaQueryResult [result]
  QueryResult
  (getResult [_ fieldName]
    (get result (keyword fieldName)))
  Object
  (toString [_] (.toString result)))

(defn- run-query
  [session name args]
  (let [query-var (or (resolve (symbol name))
                    (throw (IllegalArgumentException.
                             (str "Unable to resolve symbol to query: " name))))

        ;; Keywordize string keys from Java.
        keyword-args (into {}
                       (for [[k v] args]
                         [(keyword k) v]))
        results (eng/query session (deref query-var) keyword-args)]
    (map #(JavaQueryResult. %) results)))

(deftype JavaWorkingMemory [session]
  WorkingMemory

  (insert [this facts]
    (JavaWorkingMemory. (apply concrete/insert session facts)))

  (retract [this facts]
    (JavaWorkingMemory. (apply concrete/retract session facts)))

  (fireRules [this]
    (JavaWorkingMemory. (concrete/fire-rules session)))

  (query [this name args]
    (run-query session name args))

  (query [this name]
    (run-query session name {})))

(defn mk-java-session
  [rulesets]
  (JavaWorkingMemory.
    (com/mk-session (map symbol rulesets))))
