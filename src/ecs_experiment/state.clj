(ns ecs-experiment.state
  (:require [ecs-experiment.components.schema :as cs]
            [ecs-experiment.utils :as utils]
            [schema.core :as s]))

;; The map nesting will get extremely silly. You might want to consider Specter
;; if it becomes painful. Plus, I've never used Specter, and I kind of want to
;; try it out.

(def Entity
  {:entity-id s/Str
   :components {s/Keyword cs/Component}})

(s/defn assoc-component :- Entity
  [entity :- Entity
   {:keys [component-key] :as component} :- cs/Component]
  (assoc-in entity [:components component-key] component))

(s/defn create-entity :- Entity
  [components :- [cs/Component]]
  (let [entity {:entity-id (utils/gen-uuid)
                :components {}}]
    (reduce assoc-component entity components)))

(def GameSystem
  {:system-key s/Keyword
   :system-fn clojure.lang.IFn})

(def State
  {:entities {s/Str Entity}
   :systems [GameSystem]})

(s/defn create-empty-state :- State
  []
  {:entities {},
   :systems []})

(s/defn add-system :- State
  [state :- State system :- GameSystem]
  (update-in state [:systems] conj system))

(s/defn get-system-fns :- [clojure.lang.IFn]
  [state :- State]
  (map :system-fn (:systems state)))

(s/defn get-entity-ids :- [s/Str]
  [state :- State]
  (keys (:entities state)))

(s/defn assoc-entity :- State
  [state :- State
   {:keys [entity-id] :as entity} :- Entity]
  (assoc-in state [:entities entity-id] entity))

(s/defn create-and-assoc-entity :- State
  [state :- State
   components :- [cs/Component]]
  (let [{:keys [entity-id] :as entity} (create-entity components)]
    (assoc-entity state entity)))

(s/defn get-component-by-entity-id :- cs/Component
  [state :- State
   entity-id :- s/Str
   component-key :- s/Keyword]
  (get-in state [:entities entity-id]))

(s/defn get-component-data-by-entity-id :- s/Any
  [state :- State
   entity-id :- s/Str
   component-key :- s/Keyword]
  (get-in state [:entities entity-id :components component-key :data]))

(s/defn assoc-component-by-entity-id :- State
  [state :- State
   entity-id :- s/Str
   {:keys [component-key] :as component} :- cs/Component]
  (assoc-in state [:entities entity-id :components component-key] component))

(s/defn dissoc-component-from-entity :- State
  [state :- State
   entity-id :- s/Str
   component-key :- s/Keyword]
  (update-in state [:entities entity-id :components] dissoc component-key))

(s/defn has-component? :- s/Any
  [entity :- Entity
   component-key :- s/Keyword]
  (get-in entity [:components component-key]))

(s/defn get-component-data :- s/Any
  [entity :- Entity
   component-key :- s/Keyword]
  (get-in entity [:components component-key :data]))
