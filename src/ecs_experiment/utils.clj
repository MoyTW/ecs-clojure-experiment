(ns ecs-experiment.utils)

(defn map-values [f m]
  (into {} (map #(vector (first %) (f (second %))) m)))

(defn gen-uuid [] (str (java.util.UUID/randomUUID)))
