(ns osrs-crafting-lookup.routes
  (:require [clojure.java.io :as io]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response]]
            [osrs-crafting-lookup.components.items :as items]
            [osrs-crafting-lookup.components.details :as details]
            [osrs-crafting-lookup.components [recipes :as recipes]
             [items :as items]
             [details :as details]]))

(defn home-routes [endpoint]
  (routes
    (wrap-json-response (GET "/api/recipes" {params :params} (recipes/get-recipes params)))
    (wrap-json-response (GET "/api/recipe" {params :params} (recipes/get-recipe params)))
    (wrap-json-response (GET "/api/items" {params :params} (items/handle params)))
    (wrap-json-response (GET "/api/details" {params :params} (details/handle params)))
    (GET "/" _
      (-> "public/index.html"
          io/resource
          io/input-stream
          response
          (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
    (resources "/")))
