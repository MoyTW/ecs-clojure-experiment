(ns ecs-experiment.systems.position
  (:require [ecs-experiment.components.position :as position-c]
            [ecs-experiment.components.schema :as cs]
            [ecs-experiment.state :as state]
            [ecs-experiment.utils :as utils]
            [schema.core :as s]))

(s/defn ^:private update-position :- state/Entity
  [max-x :- s/Int
   max-y :- s/Int
   entity :- state/Entity]
  (let [[vx vy] (state/get-component-data entity :velocity)
        [px py] (state/get-component-data entity :position)]
    (if (and vx px)
      (->> (position-c/create-position-component
            (mod (+ px vx) max-x)
            (mod (+ py vy) max-y))
           (state/assoc-component entity))
      entity)))

(s/defn ^:private position-system-fn :- state/State
  [max-x :- s/Int
   max-y :- s/Int
   state :- state/State]
  (->> (:entities state)
       (utils/map-values #(update-position max-x max-y %))
       (assoc state :entities)))

(s/defn create-position-system :- state/GameSystem
  [{:keys [:max-x :max-y]}]
  {:system-key :postion
   :system-fn #(position-system-fn max-x max-y %)})
