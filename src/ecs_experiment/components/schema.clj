(ns ecs-experiment.components.schema
  (:require [schema.core :as s]))

(def Component
  {:component-key s/Keyword
   s/Keyword s/Any})

(def PlayerComponent
  {:component-key (s/eq :player)})

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

(def Command
  (s/enum :turn-left :turn-right))

(def CommandsComponent
  {:component-key (s/eq :commands)
   :data #{Command}})
