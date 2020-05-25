(ns osrs-crafting-lookup.styles
  (:require [garden-watcher.def :refer [defstyles]]))

(def el-height "2rem")
(def side-dist "0.5rem")
(def nav-height "3rem")
(def nav-button-width "4rem")
(def primary-color "purple")
(def secondary-color "#aaa")
(def tertiary-color "#ccc")
(def quaternary-color "#eee")

(defstyles style
           [:body {:padding-top nav-height}]
           [:.flex-container {:display         "flex"
                              :flex-wrap       "wrap"
                              :justify-content "center"
                              ;:align-items "stretch"
                              }]

           ; NAV
           [:.nav-bar {:position         "absolute"
                       :top              0
                       :left             0
                       :height           nav-height
                       :width            "100%"
                       :background-color primary-color}]
           [:.nav-left {:top             "50%"
                        :transform       "translateY(-50%)"
                        :position        "absolute"
                        :left            side-dist
                        :list-style-type "none"
                        :margin          "0"
                        :padding         "0"}]
           [:.nav-left-contents {:display          "inline-block"
                                 :text-align       "center"
                                 :height           el-height
                                 :width            nav-button-width
                                 :background-color quaternary-color
                                 :margin-right     side-dist}]
           [:.nav-left-link {:display "inline-block"
                             :padding-top side-dist
                             :height  "100%"
                             :width   "100%"}]
           [:.nav-search {:position  "absolute"
                          :top       "50%"
                          :transform "translateY(-50%)"
                          :right     (str "calc(" side-dist " + " nav-button-width ")")
                          :height    el-height}]
           [:.nav-search-button {:position  "absolute"
                                 :top       "50%"
                                 :transform "translateY(-50%)"
                                 :right     side-dist
                                 :height    el-height
                                 :width     nav-button-width}]

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
           [:.results-item-price {:float "right"}]
           )
