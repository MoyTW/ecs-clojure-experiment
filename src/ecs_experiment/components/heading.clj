(ns ecs-experiment.components.heading
  (:require [ecs-experiment.components.schema :as cs]
            [schema.core :as s]))

(s/defn create-heading-component :- cs/HeadingComponent
  [degrees :- s/Num]
  {:component-key :heading
   :data (mod degrees 360)})
