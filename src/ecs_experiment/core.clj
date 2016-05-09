(ns ecs-experiment.core
  (:require [quil.core :as q]
            [schema.core :as s]))

(s/set-fn-validation! true)

;; The map nesting will get extremely silly. You might want to consider Specter
;; if it becomes painful. Plus, I've never used Specter, and I kind of want to
;; try it out.

(def Component
  {:component-key s/Keyword
   s/Keyword s/Any})

(def Entity
  {:entity-id s/Int
   :components {s/Keyword Component}})

(def State
  {:entities {s/Int Entity}
   :systems [s/Keyword]})

(s/defn assoc-component :- State
  [state :- State
   entity-id :- s/Int
   {:keys [component-key] :as component} :- Component]
  (assoc-in state [:entities entity-id :components component-key] component))

(s/defn dissoc-component :- State
  [state :- State
   entity-id :- s/Int
   component-key :- s/Keyword]
  (update-in state [:entities entity-id :components] dissoc component-key))

;; ############################### TEST DRAWING ################################

(defn setup []
  (q/frame-rate 30)
  (q/background 200))

(defn draw-random-circles []
  (q/stroke (q/random 255) (q/random 50) (q/random 255))
  (q/stroke-weight (q/random 10))
  (q/fill (q/random 255))

  (let [diam (q/random 100)
        x (q/random (q/width))
        y (q/random (q/height))]
    (q/ellipse x y diam diam)))

(defn example-window []
  (q/defsketch example
    :title "Random Red/Purple Circles"
    :setup setup
    :draw draw-random-circles
    :size [640 480]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (example-window))
