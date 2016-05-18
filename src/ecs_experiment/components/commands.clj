(ns ecs-experiment.components.commands
  (:require [ecs-experiment.components.schema :as cs]
            [schema.core :as s]))

(s/defn create-commands-component :- cs/CommandsComponent
  [commands :- #{cs/Command}]
  {:component-key :commands
   :data commands})
