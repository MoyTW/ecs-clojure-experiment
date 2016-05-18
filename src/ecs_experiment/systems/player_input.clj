(ns ecs-experiment.systems.player-input
  (:require [ecs-experiment.components.player :as player]
            [ecs-experiment.components.commands :as commands]
            [ecs-experiment.components.schema :as cs]
            [ecs-experiment.state :as state]
            [ecs-experiment.utils :as utils]
            [schema.core :as s]))

(def command-mapping
  {:left :turn-left
   :a :turn-left
   :right :turn-right
   :d :turn-right})

(s/defn input->commands :- #{cs/Command}
  [input :- #{s/Keyword}]
  (->> (map command-mapping input)
       (remove nil?)
       (into #{})))

(s/defn update-player-commands :- state/Entity
  [input-commands :- #{s/Keyword} entity :- state/Entity]
  (if (and (state/has-component? entity :player)
           (state/has-component? entity :commands))
    (->> (input->commands input-commands)
         commands/create-commands-component
         (state/assoc-component entity))
    entity))

(s/defn ^:private input-system-fn :- state/State
  [input-state :- (s/atom #{s/Keyword})
   state :- state/State]
  (if (not (empty? @input-state))
    (let [new-state (->> (:entities state)
                         (utils/map-values (partial update-player-commands
                                                    @input-state))
                         (assoc state :entities))]
      (reset! input-state #{})
      new-state)
    state))

(s/defn create-player-input-system :- clojure.lang.IFn
  [input-state :- (s/atom #{s/Keyword})]
  #(input-system-fn input-state %))
