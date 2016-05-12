(ns ecs-experiment.core
  (:require [ecs-experiment.components.heading :as heading-c]
            [ecs-experiment.components.position :as position-c]
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
              (position-c/create-position-component 100 100)
              (heading-c/create-heading-component 90)]))))

(def images (atom {}))

(def exit? (atom false))

(def test-position-system
  (position-s/create-position-system {}))

(defn- run-state []
  (dotimes [_ 50]
    (reset! test-state (test-position-system @test-state))
    (Thread/sleep 32)))

(defn setup []
  (q/image-mode :center)
  (q/frame-rate 60)
  (q/background 200)
  (swap! images assoc :dreadnought (q/load-image "Dreadnought_icon.png")))

(defn draw-state []
  (when @exit?
    (q/exit))

  (q/background 0)

  (doseq [e (vals (:entities @test-state))]
    (when-let [pos (state/get-component-data e :position)]
      (q/push-matrix)
      (q/translate (first pos) (second pos))
      (when-let [heading (state/get-component-data e :heading)]
        (q/rotate (q/radians heading)))
      (q/image (:dreadnought @images) 0 0)
      (q/pop-matrix))))

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
