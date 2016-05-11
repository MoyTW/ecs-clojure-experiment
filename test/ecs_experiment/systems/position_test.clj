(ns ecs-experiment.systems.position-test
  (:require [clojure.test :refer :all]
            [ecs-experiment.components.position :as position-c]
            [ecs-experiment.components.velocity :as velocity-c]
            [ecs-experiment.state :as state]
            [ecs-experiment.systems.position :as position-s]))

(def entity-id "test-entity")

(def base-state
  {:entities {entity-id {:entity-id entity-id,
                         :components {}}},
   :systems []})

(def test-state
  (-> base-state
      (state/assoc-component-by-entity-id
       entity-id (velocity-c/create-velocity-component 3 1))
      (state/assoc-component-by-entity-id
       entity-id (position-c/create-position-component 10 10))))

(deftest sums-velocity-and-position
  (let [position-system (position-s/create-position-system nil)
        next-state (position-system test-state)]
    (is (= [13 11]
           (state/get-component-data-by-entity-id
            next-state entity-id :position)))))
