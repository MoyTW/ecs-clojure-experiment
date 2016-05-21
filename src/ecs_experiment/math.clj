(ns ecs-experiment.math
  (:require [schema.core :as s]))

(s/defn pow :- s/Num
  [n :- s/Num e :- s/Num]
  (Math/pow n e))

(s/defn sq :- s/Num
  [n :- s/Num]
  (* n n))

(s/defn sqrt :- s/Num
  [n :- s/Num]
  (Math/sqrt n))

(def Vector2D
  [(s/one s/Num "x") (s/one s/Num "y")])

(s/defn magnitude :- s/Num
  [[x y] :- Vector2D]
  (sqrt (+ (sq x) (sq y))))

(s/defn normalize :- Vector2D
  [[x y :as v] :- Vector2D]
  (let [m (magnitude v)]
    [(/ x m) (/ y m)]))

(s/defn subtract :- Vector2D
  [[x0 y0] :- Vector2D [x1 y1] :- Vector2D]
  [(- x0 x1) (- y0 y1)])

(s/defn scale :- Vector2D
  [[x y] :- Vector2D s :- s/Num]
  [(* x s) (* y s)])
