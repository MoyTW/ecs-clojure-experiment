(ns ecs-experiment.systems.position
  (:require [ecs-experiment.components.position :as position-c]
            [ecs-experiment.components.schema :as cs]
            [ecs-experiment.state :as state]
            [ecs-experiment.utils :as utils]
            [schema.core :as s]))

(s/defn ^:private update-position :- state/Entity
  [entity :- state/Entity]
  (let [[vx vy] (state/get-component-data entity :velocity)
        [px py] (state/get-component-data entity :position)]
    (if (and vx px)
      (->> (position-c/create-position-component (+ px vx) (+ py vy))
           (state/assoc-component entity))
      entity)))

(s/defn ^:private position-system-fn :- state/State
  [state :- state/State]
  (->> (:entities state)
       (utils/map-values update-position)
       (assoc state :entities)))

(s/defn create-position-system :- state/GameSystem
  [_]
  {:system-key :postion
   :system-fn position-system-fn})
