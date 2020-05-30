(ns osrs-crafting-lookup.components.nav
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]))

(defonce search-term (atom ""))

(defn submit-search [handler name]
  (handler "/api/recipes" {:name name} ["recipes" name]))

(defn listen-enter [event cb]
  (if (= (.-key event) "Enter")
    (submit-search cb (deref search-term))))

(defn update-search-term [event]
  (swap! search-term #(str (-> event
                               .-target
                               .-value))))

(rum/defc home []
  [:ul {:class "nav-links"}
   [:li {:class "nav-link"} [:a {:class "nav-link-anchor" :href "/"} "Home"]]])

(rum/defc search [search-result-cb]
  [:div {:class "nav-search"} [:input {:class       "nav-search-input"
                                       :type        "text"
                                       :placeholder "Search..."
                                       :on-key-down #(listen-enter %1 search-result-cb)
                                       :on-change   update-search-term}]
   [:button {:class "nav-search-button" :type "submit" :on-click #(submit-search search-result-cb (deref search-term))} "Search"]])

(rum/defc nav [search-result-cb]
  [:div {:class "nav-bar"} (home)
   (search search-result-cb)])
