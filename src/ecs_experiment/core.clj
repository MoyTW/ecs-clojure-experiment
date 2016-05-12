(ns ecs-experiment.core
  (:require [ecs-experiment.components.position :as position-c]
            [ecs-experiment.components.velocity :as velocity-c]
            [ecs-experiment.state :as state]
            [ecs-experiment.systems.position :as position-s]
            [quil.core :as q]
            [schema.core :as s]))

(s/set-fn-validation! true)

;; ############################### TEST DRAWING ################################

(def test-state
  (atom (-> (state/create-empty-state)
            (state/create-and-assoc-entity
             [(velocity-c/create-velocity-component 3 1)
              (position-c/create-position-component 10 10)]))))

(def exit? (atom false))

(def test-position-system
  (position-s/create-position-system {}))

(defn- run-state []
  (dotimes [_ 50]
    (reset! test-state (test-position-system @test-state))
    (Thread/sleep 100)))

(defn setup []
  (q/frame-rate 10)
  (q/background 200))

(defn draw-state []
  (when @exit?
    (q/exit))

  (q/stroke (q/random 255) (q/random 50) (q/random 255))
  (q/stroke-weight (q/random 10))
  (q/fill (q/random 255))

  (doseq [e (vals (:entities @test-state))]
    (when-let [pos (state/get-component-data e :position)]
      (q/ellipse (first pos) (second pos) 15 15))))

(defn example-window []
  (q/defsketch example
    :title "Random Red/Purple Circles"
    :setup setup
    :draw draw-state
    :size [640 480]))

(defn example-run-and-draw []
  (example-window)
  (run-state)
  (reset! exit? true))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (example-run-and-draw))
