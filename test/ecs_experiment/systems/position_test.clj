(ns ecs-experiment.systems.position-test
  (:require [clojure.test :refer :all]
            [ecs-experiment.components.position :as position-c]
            [ecs-experiment.components.velocity :as velocity-c]
            [ecs-experiment.state :as state]
            [ecs-experiment.systems.position :as position-s]))

(def test-state
  (-> (state/create-empty-state)
      (state/create-and-assoc-entity
       [(velocity-c/create-velocity-component 3 1)
        (position-c/create-position-component 10 10)])))

(def entity-id (first (state/get-entity-ids test-state)))

(deftest sums-velocity-and-position
  (let [position-system (position-s/create-position-system nil)
        next-state (position-system test-state)]
    (is (= [13 11]
           (state/get-component-data-by-entity-id
            next-state entity-id :position)))))
