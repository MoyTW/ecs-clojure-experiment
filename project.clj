(defproject ecs-experiment "0.1.0-SNAPSHOT"
  :description "Experiment for learning how to create an Entity-Component-System game."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [prismatic/schema "1.1.1"]
                 [quil "2.4.0"]]
  :main ^:skip-aot ecs-experiment.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
