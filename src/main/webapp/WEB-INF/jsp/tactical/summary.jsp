<script type="text/javascript">
    function fixLocale(value) {
        var dsep = (1 / 2).toString().charAt(1);
        var tsep = (dsep == ".") ? "," : ".";

        var rx = new RegExp("^(\\d{1,3}(\\" + tsep + "\\d{3})*(\\" + dsep + "\\d+)?|(\\d+))(\\" + dsep + "\\d+)?$");
        var bval;
        bval = rx.test(value.replace("/(^\s*|\s*$/", ""));
        return bval;
    }

    function myParse(value) {
        //return parseFloat(fixLocale(value));
        return parseFloat(value);
    }

    function sum(value) {
        var index;
        var cinfbatt, cinfsize;
        var cartbatt, cartsize;
        var ccavbatt, ccavsize;
        var cengbatt, cengsize;
        var ctotbatt, ctotsize;
        var totBatt, maxIndex;

        cinfbatt = 0;
        cinfsize = 0;
        cartbatt = 0;
        cartsize = 0;
        ccavbatt = 0;
        ccavsize = 0;
        cengbatt = 0;
        cengsize = 0;
        totBatt = 0;
        ctotbatt = 0;
        ctotsize = 0;

        if (value == 0) {
            maxIndex = list1;
        } else {
            maxIndex = list2;
        }

        for (index = 0; index < maxIndex; index++) {
            if (document.getElementById('batt[' + value + '][' + index + ']').value == "") {
                totBatt = 0;
            } else {
                totBatt = myParse(document.getElementById('batt[' + value + '][' + index + ']').value);
            }
            document.getElementById('hc[' + value + '][' + index + ']').value = totBatt * 800;

            ctotbatt += totBatt;
            ctotsize += totBatt * 800;

            if ((document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("In") >= 0)
                    || (document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("EI") >= 0)
                    || (document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("Kt") >= 0)
                    || (document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("Co") >= 0)) {

                if (document.getElementById('name[' + value + '][' + index + ']').innerHTML.lastIndexOf("Pioneers") >= 0) {
                    cengbatt += totBatt;
                    cengsize += totBatt * 800;

                } else {
                    cinfbatt += totBatt;
                    cinfsize += totBatt * 800;
                }

            } else {
                if ((document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("Ca") >= 0)
                        || (document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("EC") >= 0)
                        || (document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("MC") >= 0)) {
                    ccavbatt += totBatt;
                    ccavsize += totBatt * 800;

                } else {
                    if ((document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("Ar") >= 0)
                            || (document.getElementById('type[' + value + '][' + index + ']').innerHTML.lastIndexOf("MA") >= 0)) {
                        cartbatt += totBatt;
                        cartsize += totBatt * 800;
                    }
                }
            }
        }

        document.getElementById('infbatt[' + value + ']').value = cinfbatt;
        document.getElementById('infsize[' + value + ']').value = cinfsize;

        document.getElementById('cavbatt[' + value + ']').value = ccavbatt;
        document.getElementById('cavsize[' + value + ']').value = ccavsize;

        document.getElementById('artbatt[' + value + ']').value = cartbatt;
        document.getElementById('artsize[' + value + ']').value = cartsize;

        document.getElementById('engbatt[' + value + ']').value = cengbatt;
        document.getElementById('engsize[' + value + ']').value = cengsize;

        document.getElementById('totbatt[' + value + ']').value = ctotbatt;
        document.getElementById('totsize[' + value + ']').value = ctotsize;
    }

</script>