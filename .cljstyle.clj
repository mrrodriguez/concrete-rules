{:files {:extensions #{"clj" "cljs" "cljc" "cljx" "edn"}
         :ignore     #{#".edn$" #".clj-kondo/imports"}}
 :rules {:indentation {:indents ^:replace {#"^[^\[]" [[:inner 0]]
                                           letfn    [[:inner 2 0]]}}
         :whitespace  {:remove-surrounding? true
                       :remove-trailing?    true
                       :insert-missing?     true}
         :types       {:enabled? false}
         :namespaces  {:enabled?    true
                       :break-libs? true
                       :indent-size 2}
         :eof-newline {:enabled? true}
         :blank-lines {:max-blank-lines 1
                       :padding-lines   1}}}
