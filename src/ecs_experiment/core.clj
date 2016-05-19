(ns ecs-experiment.core
  (:require [ecs-experiment.components.commands :as commands-c]
            [ecs-experiment.components.heading :as heading-c]
            [ecs-experiment.components.player :as player-c]
            [ecs-experiment.components.position :as position-c]
            [ecs-experiment.components.velocity :as velocity-c]
            [ecs-experiment.state :as state]
            [ecs-experiment.systems.helm :as helm-s]
            [ecs-experiment.systems.position :as position-s]
            [ecs-experiment.systems.player-input :as player-input-s]
            [quil.core :as q]
            [schema.core :as s]))

(s/set-fn-validation! true)

;; ############################### TEST DRAWING ################################

(def input-state (atom #{}))

(def test-state (atom nil))

(def exit? (atom false))

(defn- reset-state! []
  (reset! input-state #{})
  (reset! test-state
          (-> (state/create-empty-state)
              ;; Add entities
              (state/create-and-assoc-entity [])
              (state/create-and-assoc-entity
               [(commands-c/create-commands-component #{})
                (player-c/create-player-component)
                (velocity-c/create-velocity-component 3 1)
                (position-c/create-position-component 100 100)
                (heading-c/create-heading-component 45)])
              (state/create-and-assoc-entity
               [(velocity-c/create-velocity-component -3 1)
                (position-c/create-position-component 400 200)
                (heading-c/create-heading-component 273)])
              ;; Add systems
              (state/add-system (player-input-s/create-player-input-system
                                 input-state))
              (state/add-system (helm-s/create-helm-system nil))
              (state/add-system (position-s/create-position-system nil))))
  (reset! exit? false))

(def images (atom {}))

(s/defn run-systems :- state/State
  [state :- state/State]
  (reduce #(%2 %1) state (state/get-system-fns state)))

(defn- run-state []
  (dotimes [_ 50]
    (reset! test-state (run-systems @test-state))
    (Thread/sleep 32)))

(defn setup []
  (q/image-mode :center)
  (q/frame-rate 60)
  (q/background 200)
  (swap! images assoc :dreadnought (q/load-image "Dreadnought_icon.png")))

(defn handle-keypress []
  (swap! input-state conj (q/key-as-keyword)))

(defn draw-state []
  (when @exit?
    (q/exit))

  (q/background 0)

  (doseq [e (vals (:entities @test-state))]
    (when-let [pos (state/get-component-data e :position)]
      (q/push-matrix)
      (q/translate (first pos) (second pos))
      (when-let [heading (+ 180 (state/get-component-data e :heading))]
        (q/rotate (q/radians heading)))
      (q/image (:dreadnought @images) 0 0)
      (q/pop-matrix))))

(defn example-window []
  (q/defsketch example
    :title "Random Red/Purple Circles"
    :setup setup
    :draw draw-state
    :size [640 480]
    :key-pressed handle-keypress))

(defn example-run-and-draw []
  (reset-state!)
  (example-window)
  (run-state)
  (reset! exit? true))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (example-run-and-draw))
