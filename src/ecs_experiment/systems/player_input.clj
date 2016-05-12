(ns ecs-experiment.systems.player-input
  (:require [ecs-experiment.components.player :as player]
            [ecs-experiment.components.schema :as cs]
            [ecs-experiment.state :as state]
            [ecs-experiment.utils :as utils]
            [schema.core :as s]))

(s/defn ^:private input-system-fn :- state/State
  [input-state :- (s/atom #{s/Keyword})
   state :- state/State]
  ;; Select all entities with player component
  ;; Update in said entities the appropriate components, given input-state
  ;; Return state
  (if (not (empty? @input-state))
    (clojure.pprint/pprint @input-state))
  state)

(s/defn create-player-input-system :- clojure.lang.IFn
  [input-state :- (s/atom #{s/Keyword})]
  #(input-system-fn input-state %))
