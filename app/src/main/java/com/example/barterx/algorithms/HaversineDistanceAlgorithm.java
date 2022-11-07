package com.example.barterx.algorithms;

import com.example.barterx.model.ListingDto;
import com.example.barterx.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class HaversineDistanceAlgorithm {

        private List<ListingDto>  listingDtoList;
        private  Profile user;
        private int earthRadius = 6371;
        private  PriorityQueue<ListingDto> dtoQueue;

        public HaversineDistanceAlgorithm (List<ListingDto> listingDto,Profile user)
        {
           this.user = user;
           this.listingDtoList = listingDto;
           dtoQueue = new PriorityQueue<>();
        }


        // convert degrees to radians helper method
        private double ToRadian(double degree)
        {
            return degree * (double)(Math.PI / 180);
        }
        //compute distance using haversine formula
        private double GetHaversineDistance(double latA,double lonA, double latB, double lonB)
        {
            double latitudeA,latitudeB, longitudeA, longitudeB;
            latitudeA = ToRadian(latA);
            latitudeB = ToRadian(latB);
            longitudeA = ToRadian(lonA);
            longitudeB = ToRadian(lonB);

            double longitudeDifference = (longitudeB - longitudeA);
            double latitudeDifference = (latitudeB - latitudeA);
            double haversine = (double)(Math.pow(Math.sin((double)(latitudeDifference / 2)), 2) + Math.cos((double)latitudeA) * Math.cos((double)latitudeB) * Math.pow(Math.sin((double)(longitudeDifference / 2)), 2));
            double c = (double)(2 * Math.asin(Math.sqrt((double)haversine)));
            double distance = (c * earthRadius);
            return  Math.round(distance * 100.0) / 100.0;
        }

        //return a list with nearest locations
        public List<ListingDto> FindNearestUsers(double maxDistance)
        {
            List<ListingDto> nearestListing = new ArrayList<ListingDto>();
            for(ListingDto listing:listingDtoList)
            {
                if(user != null)
                {
                    listing.setDistance(GetHaversineDistance(user.getLatitude(),user.getLogitude(), listing.getLatitude(),listing.getLongitude()));
                }

            } // O(n)

            if (listingDtoList.size() > 0)
            {
                for (int i = 0; i < listingDtoList.size(); i++)  //O(n)
                {

                    ListingDto dto = listingDtoList.get(i); //O(1)
                    if (listingDtoList.get(i).getDistance() < maxDistance) //O(1)
                    {
                        dtoQueue.add(dto); // O(n)
                    }
                }
            }
            //n+n+1+1+n = 3n+2 = O(n)

            return dtoQueue.stream().collect(Collectors.toList()); // O(nlogn)
        } // O(nlogn)
}

