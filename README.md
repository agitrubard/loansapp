# Loans Payment Plan & Currency Rates Backend Application
YapıKredi & VakıfBank APIs Integration

## Loans Payment Plan

#### Example

  > `Request:` <br>
    { <br>
      "loanTerm" : 2, <br>
      "loanAmount" : 2000 <br>
    } <br>

### VakıfBank
* Get Loan Payment Plan [`Loan Calculator API Documentation`](https://apiportal.vakifbank.com.tr/en/documentation/InformationServices/loanCalculator)

   > `Response:` <br>
		{ <br>
     		"bankName": "VAKIFBANK", <br>
     		"intRate": 1.48 <br>
     		"totalInterest": 44.51, <br>
    		"monthlyCostRate": 15.2, <br>
     		"installmentAmount": 1022.25, <br>
    		"totalPaymentAmount": 2044.5 <br>
		} <br>

### YapıKredi
* Get Loan Payment Plan [`Loan Payment Plan API Documentation`](https://apiportal.yapikredi.com.tr/documentation/loans/loanPaymentPlan)

	> `Response:` <br>
		{ <br>
     		"bankName": "YAPIKREDI", <br>
     		"intRate": 1.48 <br>
     		"totalInterest": 44.51, <br>
    		"monthlyCostRate": 15.2, <br>
     		"installmentAmount": 1022.25, <br>
    		"totalPaymentAmount": 2044.5 <br>
		} <br>

### Combined
* Get Loan Payment Plans

	> `Response:` <br>
		{ <br>
			{ <br>
				"bankName": "VAKIFBANK", <br>
				"intRate": 1.48, <br>
				"totalInterest": 44.51, <br>
				"monthlyCostRate": 15.2, <br>
				"installmentAmount": 1022.25, <br>
				"totalPaymentAmount": 2044.51 <br>
			}, <br>
			{ <br>
				"bankName": "YAPIKREDI", <br>
				"intRate": 1.48, <br>
				"totalInterest": 44.51, <br>
				"monthlyCostRate": 15.2, <br>
				"installmentAmount": 1022.25, <br>
				"totalPaymentAmount": 2044.51 <br>
			} <br>
		} <br>

## Currency Rates

  > `Request:` <br>
    { <br>
    } <br>


### VakıfBank
* Get Currency Rates [`F/X Currency Rates API Documentation`](https://apiportal.vakifbank.com.tr/en/documentation/InformationServices/getCurrencyRates)

	> `Response:` <br>
		[ <br>
			{ <br>
				"currencyName": "AED", <br>
         	"sellRate": 2.091115356, <br>
         	"buyRate": 1.933910056, <br>
         	"averageRate": 2.012512706 <br>
			}, <br>
			. <br>
			. <br>
			. <br>
			{ <br>
       	   "currencyName": "EUR", <br>
    	      "sellRate": 9.274590588, <br>
    		   "buyRate": 8.856414246, <br>
    	      "averageRate": 9.065502417 <br>
			}, <br>
	 		{ <br>
     	  	   "currencyName": "GBP", <br>
       		"sellRate": 10.291438418, <br>
       		"buyRate": 9.79159723, <br>
      		"averageRate": 10.041517824 <br>
			}, <br>
			. <br>
			. <br>
			. <br>
			{ <br>
            "currencyName": "USD", <br>
            "sellRate": 7.57623, <br>
            "buyRate": 7.20828, <br>
            "averageRate": 7.392255 <br>
			}, <br>
			{ <br>
         	"currencyName": "XAU", <br>
         	"sellRate": 473.6583813, <br>
         	"buyRate": 451.8553139, <br>
         	"averageRate": 462.7568476 <br>
			}, <br>
			{ <br>
         	"currencyName": "ZAR", <br>
         	"sellRate": 0.49870258, <br>
         	"buyRate": 0.486312407, <br>
         	"averageRate": 0.492507494 <br>
			} <br>
		] <br>

### YapıKredi
* Get Currency Rates [`Currency Rates API Documentation`](https://apiportal.yapikredi.com.tr/documentation/foreignCurrency/currencyRates)

	> `Response:` <br>
		[ <br>
			{ <br>
				"currencyName": "AED", <br>
         	"sellRate": 2.091115356, <br>
         	"buyRate": 1.933910056, <br>
         	"averageRate": 2.012512706 <br>
			}, <br>
			. <br>
			. <br>
			. <br>
			{ <br>
       	   "currencyName": "EUR", <br>
    	      "sellRate": 9.274590588, <br>
    		   "buyRate": 8.856414246, <br>
    	      "averageRate": 9.065502417 <br>
			}, <br>
	 		{ <br>
     	  	   "currencyName": "GBP", <br>
       		"sellRate": 10.291438418, <br>
       		"buyRate": 9.79159723, <br>
      		"averageRate": 10.041517824 <br>
			}, <br>
			. <br>
			. <br>
			. <br>
			{ <br>
            "currencyName": "USD", <br>
            "sellRate": 7.57623, <br>
            "buyRate": 7.20828, <br>
            "averageRate": 7.392255 <br>
			}, <br>
			{ <br>
         	"currencyName": "XAU", <br>
         	"sellRate": 473.6583813, <br>
         	"buyRate": 451.8553139, <br>
         	"averageRate": 462.7568476 <br>
			}, <br>
			{ <br> 
         	"currencyName": "ZAR", <br>
         	"sellRate": 0.49870258, <br>
         	"buyRate": 0.486312407, <br>
         	"averageRate": 0.492507494 <br>
			} <br>
		] <br>

## You need the application.yaml file for run the application

  > `For Example:` <br>
    vakifbankconfig: <br>
      client-id: #YapiKredi Personal Client ID <br>
			client-secret: #YapiKredi Personal Client Secret <br>
			token-url: https://apigw.vakifbank.com.tr:8443/auth/oauth/v2/token <br>
			loan-url: https://apigw.vakifbank.com.tr:8443/loanCalculator <br>
			currency-rates-url: https://apigw.vakifbank.com.tr:8443/getCurrencyRates <br>
      <br>
		yapikrediconfig: <br>
			client-id: #YapiKredi Personal Client ID <br>
			client-secret: #YapiKredi Personal Client Secret <br>
			token-url: https://api.yapikredi.com.tr/auth/oauth/v2/token <br>
			loan-url: https://api.yapikredi.com.tr/api/credit/calculation/v1/loanPaymentPlan <br>
			currency-rates-url: https://api.yapikredi.com.tr/api/investmentrates/v1/currencyRates <br>
