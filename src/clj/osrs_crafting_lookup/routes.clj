(ns osrs-crafting-lookup.routes
  (:require [clojure.java.io :as io]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response]]
            [osrs-crafting-lookup.components.recipes :as recipes]))

(defn auto-wrap [to-wrap]
  (if (nil? (:body to-wrap))
    {:body to-wrap}
    to-wrap))

(defn home-routes [endpoint]
  (routes
   (wrap-json-response (GET "/api/recipes" {params :params} (auto-wrap (recipes/get-recipes params))))
   (wrap-json-response (GET "/api/recipe" {params :params} (auto-wrap (recipes/get-recipe params))))
   (GET "/" _
     (-> "public/index.html"
         io/resource
         io/input-stream
         response
         (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
   (resources "/")))
