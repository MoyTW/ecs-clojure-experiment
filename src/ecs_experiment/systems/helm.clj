(ns ecs-experiment.systems.helm
  (:require [ecs-experiment.components.commands :as commands-c]
            [ecs-experiment.components.heading :as heading-c]
            [ecs-experiment.components.velocity :as velocity-c]
            [ecs-experiment.components.schema :as cs]
            [ecs-experiment.math :as math]
            [ecs-experiment.state :as state]
            [ecs-experiment.utils :as utils]
            [schema.core :as s]))

(s/defn ^:private forward :- state/Entity
  [entity :- state/Entity]
  (let [degrees (state/get-component-data entity :heading)
        v (state/get-component-data entity :velocity)]
    (if (and degrees v)
      (->> (math/degrees->vector degrees 0.1)
           (math/add v)
           (apply velocity-c/create-velocity-component)
           (state/assoc-component entity))
      entity)))

(s/defn ^:private turn :- state/Entity
  [degrees :- s/Num entity :- state/Entity]
  (if-let [old-degrees (state/get-component-data entity :heading)]
    (->> (heading-c/create-heading-component (+ degrees old-degrees))
         (state/assoc-component entity))
    entity))

(s/defn ^:private stop :- state/Entity
  [entity :- state/Entity]
  (if-let [v (state/get-component-data entity :velocity)]
    (->> (math/scale v 0.5)
         (apply velocity-c/create-velocity-component)
         (state/assoc-component entity))
    entity))

(def ^:private commands->fns
  {:forward forward
   :turn-left #(turn -1 %)
   :turn-right #(turn 1 %)
   :stop stop})

;; This is kinda clunky! Aesthetically unpleasing!
(s/defn ^:private update-helm :- state/Entity
  [entity :- state/Entity]
  (if-let [commands (state/get-component-data entity :commands)]
    ;; Run commands
    (let [commanded (->> (map commands->fns commands)
                         (reduce #(%2 %1) entity))]
      ;; Clear commands
      (->> (commands-c/create-commands-component #{})
           (state/assoc-component commanded)))
    entity))

(s/defn ^:private helm-system-fn :- state/State
  [state :- state/State]
  (->> (:entities state)
       (utils/map-values update-helm)
       (assoc state :entities)))

(s/defn create-helm-system :- state/GameSystem
  [_]
  {:system-key :helm
   :system-fn helm-system-fn})
