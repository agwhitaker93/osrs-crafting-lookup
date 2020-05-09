(ns osrs-crafting-lookup.components.nav
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]))

(defonce search-term (atom ""))

(defn submit-search [handler]
  (GET "/api/items" {:params  {:query (deref search-term)}
                      :handler #(-> %1
                                    ((fn [to-parse] (.parse js/JSON to-parse)))
                                    (js->clj :keywordize-keys true)
                                    (handler))}))

(defn listen-enter [event cb]
  (if (= (.-key event) "Enter")
    (submit-search cb)))

(defn update-search-term [event]
  (swap! search-term #(str (-> event
                               .-target
                               .-value))))

(rum/defc home []
  [:button {:class "nav-inner nav-home"} "Home"])

(rum/defc search [search-result-cb]
  [:div {} [:input {:class       "nav-inner nav-search"
                    :type        "text"
                    :placeholder "Search..."
                    :on-key-down #(listen-enter %1 search-result-cb)
                    :on-change   update-search-term}]
   [:button {:class    "nav-inner nav-search-button"
             :on-click #(submit-search search-result-cb)} "Search"]])

(rum/defc nav [search-result-cb]
  [:div {:class "nav-bar"} (home)
   (search search-result-cb)])
