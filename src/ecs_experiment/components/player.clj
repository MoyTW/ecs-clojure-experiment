(ns ecs-experiment.components.player
  (:require [ecs-experiment.components.schema :as cs]
            [schema.core :as s]))

(s/defn create-player-component :- cs/PlayerComponent
  []
  {:component-key :player})
