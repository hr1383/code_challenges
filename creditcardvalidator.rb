   require 'json'


# credit card validator using Luhn algorithm

def ccValid(ccNumber)

	ccNumber = ccNumber.reverse
	even,odd = ccNumber.split("").partition.with_index { |_, index| (index).even? }
	sum=0
	even.each do |digit|
	   sum+=digit.to_i
	end
	odd.each do |digit|
      product = digit.to_i* 2
      if product >= 10
         sum+= product/10+product%10
      else
         sum+=product;
      end
	end

	return sum > 0 && sum%10 == 0
end


filename = ARGV.first

if filename == nil
   abort("Enter the file name to read the credit card numbers")
end
txt = File.open(filename)


json = JSON.parse(txt.read())

	map = Hash.new
	map['3'] = 'amex'
	map['4'] = 'visa'
	map['5'] = 'mastercard'
	map['6'] = 'discover'
	big_array =[]
   json.each do |ccNumber|
      newmap = Hash.new
      if ccValid(ccNumber)
         newmap['valid']='true'
      else
         newmap['valid']='false'
      end
      newmap['type']= map[ccNumber[0,1]]
      big_array <<newmap	   
	end

	puts big_array.to_json
