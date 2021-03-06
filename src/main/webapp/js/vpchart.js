/**
 * Gray theme for Highcharts JS
 * @author Torstein Hønsi
 */

Highcharts.theme = {
    colors:["#c9c9a7", "#262626", "#232188", "#e5e800",
        "#c1c0ff", "#ff0000", "#fe5200", "#12ffff", "#c4ffb9",
        "#086d6e", "#be81ff", "#0000ff", "#116d00", "#ffc400", "#28ff00",
        "#fa00ff", "#575287"],
    chart:{
        backgroundColor:{
            linearGradient:[0, 0, 0, 400],
            stops:[
                [0, 'rgba(243, 243, 243,0.1)'],
                [1, 'rgba(243, 243, 243,0.8)']
            ]
        },
        borderWidth:0,
        borderRadius:15,
        plotBackgroundColor:null,
        plotShadow:false,
        plotBorderWidth:0
    },
    title:{
        style:{
            color:'#4c0000',
            fontWeight:'bold',
            font:'16px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
        }
    },
    subtitle:{
        style:{
            color:'#490100',
            font:'12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
        }
    },
    xAxis:{
        gridLineWidth:0,
        lineColor:'#4c0000',
        tickColor:'#4c0000',
        labels:{
            style:{
                color:'#490100',
                fontWeight:'bold'
            }
        },
        title:{
            style:{
                color:'#4c0000',
                font:'bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
            }
        }
    },
    yAxis:{
        alternateGridColor:null,
        minorTickInterval:null,
        gridLineColor:'rgba(100, 0, 0, .1)',
        lineWidth:0,
        tickWidth:0,
        labels:{
            style:{
                color:'#490100',
                fontWeight:'bold'
            }
        },
        title:{
            style:{
                color:'#6c0000',
                font:'bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
            }
        }
    },
    legend:{
        itemStyle:{
            color:'#200000'
        },
        itemHoverStyle:{
            color:'#490100'
        },
        itemHiddenStyle:{
            color:'#333'
        }
    },
    labels:{
        style:{
            color:'#6c0000'
        }
    },
    tooltip:{
        backgroundColor:{
            linearGradient:[0, 0, 0, 50],
            stops:[
                [0, 'rgba(96, 96, 96, .8)'],
                [1, 'rgba(16, 16, 16, .8)']
            ]
        },
        borderWidth:0,
        style:{
            color:'#FFF'
        }
    },


    plotOptions:{
        line:{
            dataLabels:{
                color:'#CCC'
            },
            marker:{
                lineColor:'#333'
            }
        },
        spline:{
            marker:{
                lineColor:'#333'
            }
        },
        scatter:{
            marker:{
                lineColor:'#333'
            }
        },
        candlestick:{
            lineColor:'white'
        }
    },

    toolbar:{
        itemStyle:{
            color:'#CCC'
        }
    },

    navigation:{
        buttonOptions:{
            backgroundColor:{
                linearGradient:[0, 0, 0, 20],
                stops:[
                    [0.4, '#606060'],
                    [0.6, '#333333']
                ]
            },
            borderColor:'#000000',
            symbolStroke:'#C0C0C0',
            hoverSymbolStroke:'#FFFFFF'
        }
    },

    exporting:{
        buttons:{
            exportButton:{
                symbolFill:'#55BE3B'
            },
            printButton:{
                symbolFill:'#7797BE'
            }
        }
    },

    // scroll charts
    rangeSelector:{
        buttonTheme:{
            fill:{
                linearGradient:[0, 0, 0, 20],
                stops:[
                    [0.4, '#888'],
                    [0.6, '#555']
                ]
            },
            stroke:'#000000',
            style:{
                color:'#CCC',
                fontWeight:'bold'
            },
            states:{
                hover:{
                    fill:{
                        linearGradient:[0, 0, 0, 20],
                        stops:[
                            [0.4, '#BBB'],
                            [0.6, '#888']
                        ]
                    },
                    stroke:'#000000',
                    style:{
                        color:'white'
                    }
                },
                select:{
                    fill:{
                        linearGradient:[0, 0, 0, 20],
                        stops:[
                            [0.1, '#000'],
                            [0.3, '#333']
                        ]
                    },
                    stroke:'#000000',
                    style:{
                        color:'yellow'
                    }
                }
            }
        },
        inputStyle:{
            backgroundColor:'#333',
            color:'silver'
        },
        labelStyle:{
            color:'silver'
        }
    },

    navigator:{
        handles:{
            backgroundColor:'#666',
            borderColor:'#AAA'
        },
        outlineColor:'#CCC',
        maskFill:'rgba(16, 16, 16, 0.5)',
        series:{
            color:'#7798BF',
            lineColor:'#A6C7ED'
        }
    },

    scrollbar:{
        barBackgroundColor:{
            linearGradient:[0, 0, 0, 20],
            stops:[
                [0.4, '#888'],
                [0.6, '#555']
            ]
        },
        barBorderColor:'#CCC',
        buttonArrowColor:'#CCC',
        buttonBackgroundColor:{
            linearGradient:[0, 0, 0, 20],
            stops:[
                [0.4, '#888'],
                [0.6, '#555']
            ]
        },
        buttonBorderColor:'#CCC',
        rifleColor:'#FFF',
        trackBackgroundColor:{
            linearGradient:[0, 0, 0, 10],
            stops:[
                [0, '#000'],
                [1, '#333']
            ]
        },
        trackBorderColor:'#666'
    },

    // special colors for some of the demo examples
    legendBackgroundColor:'rgba(48, 48, 48, 0.8)',
    legendBackgroundColorSolid:'rgb(70, 70, 70)',
    dataLabelsColor:'#444',
    textColor:'#E0E0E0',
    maskColor:'rgba(255,255,255,0.3)'
};

// Apply the theme
var highchartsOptions = Highcharts.setOptions(Highcharts.theme);
