(defproject org.metasimple/concrete-rules "0.1.0"
  :description "A deterministic Rete rules engine for Clojure."
  :url "https://metasimple.org/concrete-rules"
  :license {:name "Apache License Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.12.4"]
                 [prismatic/schema "1.4.1"]]
  :profiles {:dev {:dependencies [[org.clojure/math.combinatorics "0.3.2"]
                                  [org.clojure/data.fressian "1.1.1"]
                                  [clj-kondo/clj-kondo "2026.01.19"]]
                   :java-source-paths ["src/test/java"]
                   :global-vars {*warn-on-reflection* true}}}
  :plugins [[com.github.clj-kondo/lein-clj-kondo "2026.01.19"]]
  :aliases {"clj-kondo-deps" ["clj-kondo" "--copy-configs" "--dependencies" "--parallel" "--lint" "$classpath"]
            "clj-kondo-lint" ["do" ["clj-kondo-deps"] ["clj-kondo" "--lint" "src/main:src/test" "--fail-level" "error"]]}
  :source-paths ["src/main/clojure"]
  :resource-paths ["clj-kondo"]
  :test-paths ["src/test/clojure"]
  :java-source-paths ["src/main/java"]
  :javac-options ["--release" "11"]
  :clean-targets ^{:protect false} ["target"]
  :repl-options {:timeout 180000}
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
  :deploy-repositories [["releases" {:url "https://repo.clojars.org"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD
                                     :sign-releases true}]
                        ["snapshots" {:url "https://repo.clojars.org"
                                      :username :env/CLOJARS_USERNAME
                                      :password :env/CLOJARS_PASSWORD
                                      :sign-releases true}]])
