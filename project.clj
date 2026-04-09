(defproject org.metasimple/concrete-rules "0.1.0-SNAPSHOT"
  :description "A deterministic Rete rules engine for Clojure."

  :url "https://metasimple.org/concrete-rules"

  :license {:name "Apache License Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0"}

  :dependencies [[org.clojure/clojure "1.11.2"]
                 [prismatic/schema "1.1.6"]]

  :profiles {:dev {:dependencies [[org.clojure/math.combinatorics "0.1.3"]
                                  [org.clojure/data.fressian "0.2.1"]
                                  [clj-kondo/clj-kondo "2023.04.14"]]
                   :java-source-paths ["src/test/java"]
                   :global-vars {*warn-on-reflection* true}}

             :recent-clj {:dependencies [^:replace [org.clojure/clojure "1.11.2"]]}
             :java9 {:jvm-opts ["--add-modules=java.xml.bind"]}}
  :plugins [[com.github.clj-kondo/lein-clj-kondo "0.2.4" :exclusions [org.clojure/clojure
                                                                      org.clojure/clojurescript]]]
  :aliases {"clj-kondo-deps" ["clj-kondo" "--copy-configs" "--dependencies" "--parallel" "--lint" "$classpath"]
            "clj-kondo-lint" ["do" ["clj-kondo-deps"] ["clj-kondo" "--lint" "src/main:src/test" "--fail-level" "error"]]}

  :javadoc-opts {:package-names "metasimple.concrete.rules"}
  :source-paths ["src/main/clojure"]
  :resource-paths ["clj-kondo"]
  :test-paths ["src/test/clojure" "src/test/common"]
  :java-source-paths ["src/main/java"]
  :javac-options ["-target" "1.8" "-source" "1.8"]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :repl-options {:timeout 180000}

  ;; Factoring out the duplication of this test selector function causes an error,
  ;; perhaps because Leiningen is using this as uneval'ed code.
  ;; For now just duplicate the line.
  :test-selectors {:default (complement (fn [x]
                                          (let [blacklisted-packages #{"generative" "performance"}
                                                patterns (into []
                                                               (comp
                                                                 (map #(str "^metasimple\\.concrete\\." % ".*"))
                                                                 (interpose "|"))
                                                               blacklisted-packages)]
                                            (some->> x :ns ns-name str (re-matches (re-pattern (apply str patterns)))))))
                   :generative (fn [x] (some->> x :ns ns-name str (re-matches #"^metasimple\.concrete\.generative.*")))
                   :performance (fn [x] (some->> x :ns ns-name str (re-matches #"^metasimple\.concrete\.performance.*")))}

  :scm {:name "git"
        :url "https://github.com/mrrodriguez/concrete-rules"}
  :pom-addition [:developers [:developer
                              [:id "mrrodriguez"]
                              [:name "Mike Rodriguez"]
                              [:url "https://metasimple.org"]]]
  :deploy-repositories [["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                      :creds :gpg}]
                        ["releases" {:url "https://repo.clojars.org"
                                     :creds :gpg
                                     :sign-releases false}]])
