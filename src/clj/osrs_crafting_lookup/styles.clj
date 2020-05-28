(ns osrs-crafting-lookup.styles
  (:require [garden-watcher.def :refer [defstyles]]))

(def primary-color "purple")
(def secondary-color "#aaa")
(def tertiary-color "#ccc")
(def quaternary-color "#eee")

(defstyles style
  [:body {:margin 0}]
  [:.flex-container {:display         "flex"
                     :flex-wrap       "wrap"
                     :justify-content "center"}]

  ; NAV
  [:.nav-bar {:background-color primary-color
              :overflow         "hidden"}]
  [:.nav-links {:float           "left"
                :list-style-type "none"
                :margin          "0"
                :padding         "0"}]
  [:.nav-link {}]
  [:.nav-link-anchor {:background-color quaternary-color
                      :display          "block"
                      :padding          "0.8rem 1rem"}
   [:&:hover {:background-color tertiary-color}]]
  [:.nav-search {:float        "right"
                 :margin-top   "0.3rem"
                 :margin-right "0.3rem"}]
  [:.nav-search-input {:border  "none"
                       :padding "0.6rem"}]
  [:.nav-search-button {:display          "inline-block"
                        :border           "none"
                        :padding          "0.6rem"
                        :cursor           "pointer"
                        :background-color quaternary-color}
   [:&:hover {:background-color tertiary-color}]]

  ; RESULTS
  [:.results-item {:position   "relative"
                   :box-shadow "0 4px 8px 0 rgba(0,0,0,0.2)"
                   :transition "0.3s"
                   :width      "300px"
                   :height     "140px"
                   :margin     "10px"}]
  ;; HEADER
  [:.results-item-header {:background-color secondary-color
                          :height           "40px"}]
  [:.results-item-icon {:float            "left"
                        :padding          "4px"
                        :background-color quaternary-color
                        :width            "32px"}]
  [:.results-item-name {:position    "absolute"
                        :line-height "40px"
                        :text-align  "center"
                        :width       "100%"}]

  ;; BODY
  [:.results-item-description {:background-color tertiary-color
                               :height           "75px"
                               :padding          "5px 5px 0 5px"}]

  ;; FOOTER
  [:.results-item-footer {:height "20px"
                          :margin "0 5px"}]
  [:.results-item-members {:float "left"}]
  [:.results-item-price {:float "right"}])
