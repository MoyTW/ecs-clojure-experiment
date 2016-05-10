(ns ecs-experiment.components.position
  (:require [ecs-experiment.components.schema :as cs]
            [schema.core :as s]))

(s/defn create-position-component :- cs/PositionComponent
  [x :- s/Num y :- s/Num]
  {:component-key :position
   :data [x y]})
