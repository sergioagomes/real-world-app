(ns real-world-api.core
  (:require [real-world-api.config :as config] 
            [real-world-api.components.examplecomponent :as example-component]
            [com.stuartsierra.component :as component]
            [real-world-api.components.pedestalcomponent :as pedestal-component]))

;; define a function to use the component
(defn real-world-system
  [config]
  (component/system-map
   :example-component (example-component/new-example-component
                       config)
   :pedestal-component 
   (component/using
    (pedestal-component/new-pedestal-component config)
    [:example-component])))


(defn -main 
  []
  (let [system (-> (config/read-config)
                   (real-world-system)
                   (component/start-system))] 
    (println "Start Real World App With Config" )
    (.addShutdownHook
     (Runtime/getRuntime)
     (new Thread #(component/stop-system system)))))