# concrete-rules

**`concrete-rules`** Forward-chaining rules for Clojure and the JVM. It is a sovereign fork of the original [Clara Rules](https://github.com/oracle-samples/clara-rules) project.

### History & Attribution

This project is a derivative work based on Clara Rules, originally developed by Cerner and Oracle. All original code remains Copyright (c) 2018-2025 Oracle and/or its affiliates. All subsequent improvements and maintenance are Copyright (c) 2026 Michael Rodriguez.

---

## _About_

concrete-rules is a forward-chaining rules engine written in Clojure with Java interoperability. It aims to simplify code with a developer-centric approach to expert systems.

## _Usage_

Here's a simple example:

```clj
(ns metasimple.concrete.support-example
  (:require [concrete.rules :refer :all]))

(defrecord SupportRequest [client level])

(defrecord ClientRepresentative [name client])

(defrule is-important
  "Find important support requests."
  [SupportRequest (= :high level)]
  =>
  (println "High support requested!"))

(defrule notify-client-rep
  "Find the client representative and send a notification of a support request."
  [SupportRequest (= ?client client)]
  [ClientRepresentative (= ?client client) (= ?name name)] ; Join via the ?client binding.
  =>
  (println "Notify" ?name "that"  ?client "has a new support request!"))

;; Run the rules! We can just use Clojure's threading macro to wire things up.
(-> (mk-session)
    (insert (->ClientRepresentative "Alice" "Acme")
            (->SupportRequest "Acme" :high))
    (fire-rules))

;;;; Prints this:

;; High support requested!
;; Notify Alice that Acme has a new support request!
```

## _Building_

Concrete is built, tested, and deployed using [Leiningen](http://leiningen.org).

## _Availability_

concrete-rules releases are on [Clojars](https://clojars.org/). Simply add the following to your project:

[![Clojars Project](http://clojars.org/org.metasimple/concrete-rules/latest-version.svg)](http://clojars.org/org.metasimple/concrete-rules)

## _Communication_

Questions can be posted to the [Slack channel](https://clojurians.slack.com/messages/clara/).

## Contributing

This project welcomes contributions from the community. Before submitting a pull request, please [review our contribution guide](./CONTRIBUTING.md)

## License

Copyright (c) 2026 Michael Rodriguez / org.metasimple

Copyright (c) 2018, 2025 Oracle and/or its affiliates.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

&nbsp;&nbsp;&nbsp;&nbsp;http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


