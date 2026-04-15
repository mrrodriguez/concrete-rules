# Concrete Rules DSL Examples

Concrete Rules provides a powerful Domain Specific Language (DSL) for defining rules and queries. While most users prefer the DSL, rules can also be constructed programmatically using Clojure maps.

## Rules

A rule consists of a Left-Hand Side (LHS), which specifies the conditions for the rule to fire, and a Right-Hand Side (RHS), which defines the actions to take when those conditions are met.

### Single Fact Match

The simplest rule matches a single fact of a specific type.

```clojure
(defrule low-temperature
  "Matches any Temperature fact with a value less than 20."
  [Temperature (< temperature 20)]
  =>
  (println "It is cold!"))
```

### Fact-Level Bindings

You can bind the entire fact to a variable to use it in the RHS or other conditions.

```clojure
(defrule capture-cold-fact
  "Binds the Temperature fact to the variable ?t."
  [?t <- Temperature (< temperature 20)]
  =>
  (insert! (->Cold ?t)))
```

### Field-Level Bindings

Specific fields within a fact can be bound to variables for joining or use in the RHS.

```clojure
(defrule capture-temperature-value
  "Binds the temperature field to ?temp."
  [Temperature (< temperature 20) (= ?temp temperature)]
  =>
  (println "The temperature is" ?temp))
```

### Logical Operators: `:not` and `:or`

Concrete Rules supports logical operators to express complex conditions.

#### `:not` (Negation)

Matches the absence of a fact.

```clojure
(defrule no-paperwork
  "Fires if there is a WorkOrder but no corresponding ApprovalForm."
  [WorkOrder (= ?id id)]
  [:not [ApprovalForm (= ?id work-order-id)]]
  =>
  (insert! (->ValidationError :missing-approval ?id)))
```

#### `:or` (Disjunction)

Matches if any of the provided conditions are true.

```clojure
(defrule extreme-weather
  "Matches if it is either very cold or very hot."
  [:or
   [Temperature (< temperature 0)]
   [Temperature (> temperature 100)]]
  =>
  (println "Extreme weather detected!"))
```

### Accumulators

Accumulators allow you to perform operations across multiple matching facts, such as counting, summing, or finding the minimum/maximum.

```clojure
(require '[metasimple.concrete.rules.accumulators :as acc])

(defrule average-temperature
  "Calculates the average temperature for a location."
  [?avg <- (acc/average :temperature) :from [Temperature (= ?loc location)]]
  =>
  (println "Average temperature in" ?loc "is" ?avg))
```

Common accumulators include `acc/all`, `acc/count`, `acc/sum`, `acc/min`, `acc/max`, and `acc/distinct`.

### Joining Patterns

Rules often join multiple facts based on shared variable bindings.

```clojure
(defrule cold-and-windy
  "Joins Temperature and WindSpeed facts on the 'location' field."
  [Temperature (= ?loc location) (< temperature 20)]
  [WindSpeed (= ?loc location) (> windspeed 30)]
  =>
  (insert! (->ColdAndWindy ?loc)))
```

## Queries

Queries have a similar structure to the LHS of a rule but are used to search the session for facts. They can take parameters that the caller binds at runtime.

```clojure
(defquery get-temps-by-location
  "Finds all temperatures for a given location parameter."
  [:?loc]
  [Temperature (= ?loc location) (= ?t temperature)])
```

You can run this query against a session:
```clojure
(query session get-temps-by-location :?loc "MCI")
;; Returns a sequence of maps, e.g., ({:?loc "MCI" :?t 15} {:?loc "MCI" :?t 10})
```

## Programmatic Rule Building

Under the hood, the DSL macros translate into Clojure maps. You can build these maps manually if you need to generate rules dynamically.

### Rule Map Structure

```clojure
{:name "example-namespace/my-rule"
 :lhs [{:type Temperature
        :constraints ['(< temperature 20)]
        :fact-binding :?t}]
 :rhs '(println "Cold fact:" ?t)}
```

### Query Map Structure

```clojure
{:name "example-namespace/my-query"
 :lhs [{:type Temperature
        :constraints ['(= ?loc location) '(= ?t temperature)]}]
 :params #{:?loc}}
```
