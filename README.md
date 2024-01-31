# mm
Some assumptions:
+ To put it simply, I don't deal with the database layer.
+ I use a simple solution to combat race conditions when paying multiple bills at the same time.
+ In this method (pay multi bills), I install other requirements of yours. I don't check if my wallet balance is enough to pay my total bills, but I process paying each bill to make sure more bills are paid as soon as possible