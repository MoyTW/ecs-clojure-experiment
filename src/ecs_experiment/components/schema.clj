(ns ecs-experiment.components.schema
  (:require [schema.core :as s]))

(def Component
  {:component-key s/Keyword
   s/Keyword s/Any})

(def PositionComponent
  {:component-key (s/eq :position)
   :data [(s/one s/Num "x") (s/one s/Num "y")]})

(def VelocityComponent
  {:component-key (s/eq :velocity)
   :data [(s/one s/Num "x") (s/one s/Num "y")]})

(defn- degrees-in-360 [degrees]
  (and (<= 0 degrees) (< degrees 360)))

(def HeadingComponent
  {:component-key (s/eq :heading)
   :data (s/constrained s/Num degrees-in-360)})
