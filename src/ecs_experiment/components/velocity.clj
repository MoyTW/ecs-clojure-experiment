(ns ecs-experiment.components.velocity
  (:require [ecs-experiment.components.schema :as cs]
            [schema.core :as s]))

(s/defn create-velocity-component :- cs/VelocityComponent
  [x :- s/Num y :- s/Num]
  {:component-key :velocity
   :data [x y]})
